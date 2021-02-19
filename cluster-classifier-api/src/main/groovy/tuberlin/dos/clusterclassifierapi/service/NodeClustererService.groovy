package tuberlin.dos.clusterclassifierapi.service


import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.ml.clustering.CentroidCluster
import org.apache.commons.math3.ml.clustering.Cluster
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer
import org.apache.commons.math3.stat.descriptive.rank.Percentile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tuberlin.dos.clusterclassifierapi.entities.DoublePointWithLabel
import tuberlin.dos.clusterclassifierapi.entities.NodeRepresentation
import tuberlin.dos.clusterclassifierapi.entities.SilhouetteScore
import tuberlin.dos.clusterclassifierapi.entities.enums.NodeLabel
import tuberlin.dos.clusterclassifierapi.repository.NodeRepresentationRepository

import javax.annotation.PostConstruct
import java.util.stream.Collectors

@Service
class NodeClustererService {

    private static final Logger logger = LoggerFactory.getLogger(NodeClustererService.class)

    private final NodeRepresentationRepository nodeRepresentationRepository

    ArrayList<CentroidCluster<DoublePointWithLabel>> cluster

    private List<NodeRepresentation> nodeRepresentationList

    private KubernetesNodeLabeller kubernetesNodeLabeller

    NodeClustererService(NodeRepresentationRepository nodeRepresentationRepository, KubernetesNodeLabeller kubernetesNodeLabeller) {
        this.nodeRepresentationRepository = nodeRepresentationRepository
        this.kubernetesNodeLabeller = kubernetesNodeLabeller
    }

    @PostConstruct
    private void determineClusters() {
        logger.info("In determineClusters() method")
        nodeRepresentationList = nodeRepresentationRepository.findAll()

        if (nodeRepresentationList.size() < 3) {
            return
        }

        List<DoublePointWithLabel> points = new ArrayList<DoublePointWithLabel>()
        points.addAll(extractPerformanceMetrics(nodeRepresentationList))


        SortedMap<Integer, Cluster<DoublePointWithLabel>> clusterMap = new TreeMap<>()
        SortedMap<Integer, Double> mapWithScore = new TreeMap<>()

        SilhouetteScore silhouetteScore = new SilhouetteScore()

        def runs = points.size() < 10 ? points.size() : 10;

        for (int i = 3; i < runs; i++) {

            KMeansPlusPlusClusterer kMeansPlusPlusClusterer = new KMeansPlusPlusClusterer(i)
            List<Cluster<DoublePointWithLabel>> list = kMeansPlusPlusClusterer.cluster(points)
            clusterMap.put(i, list)
            mapWithScore.put(i, silhouetteScore.score(list))
        }
        logger.info("Evaluate silhouetteScore score to determine best number of clusters")
        cluster = clusterMap.get(mapWithScore.sort().firstKey())

        labelNodes()
        kubernetesNodeLabeller.labelNodes(this.nodeRepresentationList)
    }

    private List<DoublePointWithLabel> extractPerformanceMetrics(List<NodeRepresentation> nodeRepresentationList) {
        logger.info("Extract node performance metrics to cluster features in form of DoublePoints")
        List<DoublePointWithLabel> doublePointList = new ArrayList<>()

        nodeRepresentationList.forEach((nodeRep) -> {
            double[] d = new double[3]
            d[0] = nodeRep.getCpu().getCpu_speed_st_events_per_second()
            d[1] = nodeRep.getCpu().getCpu_speed_mt_events_per_second()
            d[2] = nodeRep.getRam().getRam_speed()
          //  d[3] = nodeRep.getIo().getSequ_read_bw()
          //  d[4] = nodeRep.getIo().getSequ_write_bw()
           // d[5] = nodeRep.getIo().getRand_read_bw()
           // d[6] = nodeRep.getIo().getRand_write_bw()
            doublePointList.add(new DoublePointWithLabel(d, nodeRep.node_ip))
        })

        return doublePointList
    }

    private void labelNodes() {
        logger.info("Start labelling nodes")
        List<double[]> doubles = this.cluster.stream().map(c -> c.center.point).collect(Collectors.toList())

        def dMatrx = new double[doubles.size()][doubles.get(0).size()]

        for (int i = 0; i < doubles.size(); i++) {
            dMatrx[i] = doubles.get(i)
        }

        RealMatrix matrix = MatrixUtils.createRealMatrix(dMatrx)

        List<Double[]> feature_quartiles = new ArrayList<>()

        for (int i = 0; i < doubles.get(0).size(); i++) {
            def data = matrix.getColumn(i)
            Arrays.sort(data)

            Percentile p_025 = new Percentile(25)
            def q1 = p_025.evaluate(data)
            Percentile p_050 = new Percentile(50)
            def q2 = p_050.evaluate(data)
            Percentile p_075 = new Percentile(75)
            def q3 = p_075.evaluate(data)

            def single_quartiles = [q1, q2, q3]
            feature_quartiles.add(single_quartiles)
        }

        RealMatrix label_matrix = matrix.copy()

        for (int i = 0; i < doubles.get(0).size(); i++) {
            for (int j = 0; j < doubles.size(); j++) {
                label_matrix.setEntry(j, i, evaluateQuartileData(feature_quartiles.get(i), label_matrix.getEntry(j, i)))
            }
        }

        for (int i = 0; i < cluster.size(); i++) {
            for (int j = 0; j < cluster.get(i).points.size(); j++) {

                NodeRepresentation toSaveRep = nodeRepresentationRepository.findById(cluster.get(i).points.get(j).label).get()
                // Ersetzen durh abgleichen mit Liste
                toSaveRep.nodeLabels.clear()
                assignLabelsToNodes(label_matrix.getRow(i), toSaveRep)

            }
        }

        logger.info("Finished labelling nodes")
    }

    private int evaluateQuartileData(List<Double[]> quartiles, double feature_value) {
        if (feature_value <= quartiles.get(0)) {
            return -1
        } else if (feature_value >= quartiles.get(2)) {
            return 1
        } else {
            return 0
        }


    }

    private void assignLabelsToNodes(double[] row, NodeRepresentation nodeRepresentation) {

        if (row[0] == 1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.CPU_ST_2)
        } else if (row[0] == 0) {
            nodeRepresentation.nodeLabels.add(NodeLabel.CPU_ST_1)
        } else if (row[0] == -1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.CPU_ST_0)
        }

        if (row[1] == 1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.CPU_MT_2)
        } else if (row[1] == 0) {
            nodeRepresentation.nodeLabels.add(NodeLabel.CPU_MT_1)
        } else if (row[1] == -1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.CPU_MT_0)
        }

        if (row[2] == 1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAM_2)
        } else if (row[2] == 0) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAM_1)
        } else if (row[2] == -1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAM_0)
        }
/*
        if (row[3] == 1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.SEQ_R_2)
        } else if (row[3] == 0) {
            nodeRepresentation.nodeLabels.add(NodeLabel.SEQ_R_1)
        } else if (row[3] == -1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.SEQ_R_0)
        }

        if (row[4] == 1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.SEQ_W_2)
        } else if (row[4] == 0) {
            nodeRepresentation.nodeLabels.add(NodeLabel.SEQ_W_1)
        } else if (row[4] == -1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.SEQ_W_0)
        }

        if (row[5] == 1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAND_R_2)
        } else if (row[5] == 0) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAND_R_1)
        } else if (row[5] == -1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAND_R_0)
        }

        if (row[6] == 1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAND_W_2)
        } else if (row[6] == 0) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAND_W_1)
        } else if (row[6] == -1) {
            nodeRepresentation.nodeLabels.add(NodeLabel.RAND_W_0)
        }

         */

        nodeRepresentationRepository.save(nodeRepresentation)
    }


}

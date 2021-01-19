package tuberlin.dos.clusterclassifierapi.controller

import org.apache.commons.math3.ml.clustering.Cluster
import org.apache.commons.math3.ml.clustering.DoublePoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tuberlin.dos.clusterclassifierapi.service.NodeClustererService

@RestController
@RequestMapping("/cluster")
class ClusterRepresentationController {

    private static final Logger logger = LoggerFactory.getLogger(ClusterRepresentationController.class)

    private final NodeClustererService nodeClusterer

    public ClusterRepresentationController(NodeClustererService nodeClusterer) {
        this.nodeClusterer = nodeClusterer
    }


    @GetMapping
    public List<Cluster<DoublePoint>> getHardwareClusters() {
        logger.info("getHardwareClusters got called")
        return nodeClusterer.cluster
    }

}

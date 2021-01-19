package tuberlin.dos.clusterclassifierapi.entities

import org.apache.commons.math3.ml.clustering.Cluster
import org.apache.commons.math3.ml.clustering.DoublePoint
import org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator
import org.apache.commons.math3.ml.distance.EuclideanDistance

class SilhouetteScore extends ClusterEvaluator<DoublePoint> {


    SilhouetteScore() {
    }

    @Override
    double score(List<? extends Cluster<DoublePoint>> clusters) {

        EuclideanDistance euclideanDistance = new EuclideanDistance();

        ArrayList<Double> smallest_mean_distance_list = new ArrayList<>();

        def qq

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = 0; j < clusters.get(i).points.size(); j++) {

                if (clusters[i].points.size() == 1) {
                    smallest_mean_distance_list.add(new Double(0.0))
                    continue;
                }

                double average_intra_distance = clusters[i].points.stream()
                        .map((s) -> euclideanDistance.compute(s.point, clusters.get(i).points.get(j).point))
                        .reduce(0, (p1, p2) -> p1 + p2) / (clusters.get(i).points.size() - 1)

                ArrayList<Double> average_nearest_distanceList = new ArrayList<>();

                // TODO cc != clusters[i]
                clusters.stream().filter((cc) -> cc != clusters[i]).forEach((c) -> {
                    average_nearest_distanceList.add(
                            (c.points.stream().map((s) -> euclideanDistance.compute(s.point, clusters.get(i).points.get(j).point))
                                    .reduce(0, (p1, p2) -> p1 + p2)) / c.points.size())
                })

                def min_average_nearest_distance = Collections.min(average_nearest_distanceList)

                def smallest_mean_distance = (min_average_nearest_distance - average_intra_distance) / (Math.max(min_average_nearest_distance, average_intra_distance))
                smallest_mean_distance_list.add(smallest_mean_distance)
            }

        }

        smallest_mean_distance_list.stream().mapToDouble(a -> a).average().orElse(10.0)

    }
}

package tuberlin.dos.clusterclassifierapi.entities

import org.apache.commons.math3.ml.clustering.DoublePoint

class DoublePointWithLabel extends DoublePoint {

    final String label;

    DoublePointWithLabel(double[] point, String label) {
        super(point)
        this.label = label
    }


}

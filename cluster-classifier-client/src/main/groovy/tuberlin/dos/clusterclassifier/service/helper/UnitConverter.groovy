package tuberlin.dos.clusterclassifier.service.helper

import org.apache.commons.math3.util.Precision

class UnitConverter {


    public static Long convertUnitStringToBytes(String toConvertString) {

        if (toConvertString.contains("KiB")) {
            Math.round(Double.valueOf(toConvertString.split(" KiB")[0]) * 1024)
        } else if (toConvertString.contains("MiB")) {
            Math.round(Double.valueOf(toConvertString.split(" MiB")[0]) * 1024 * 1024)
        } else if (toConvertString.contains("GiB")) {
            Math.round(Double.valueOf(toConvertString.split(" GiB")[0]) * 1024 * 1024 * 1024)
        } else if (toConvertString.contains("KB")) {
            Math.round(Double.valueOf(toConvertString.split(" KB")[0]) * 1000)
        } else if (toConvertString.contains("MB")) {
            Math.round(Double.valueOf(toConvertString.split(" MB")[0]) * 1000 * 1000)
        } else if (toConvertString.contains("GB")) {
            Math.round(Double.valueOf(toConvertString.split(" GB")[0]) * 1000 * 1000 * 1000)
        }

    }

    public static Double convertUnitStringToMB(String toConvertString) {

        if (toConvertString.contains("KiB")) {
            Precision.round(Double.valueOf(toConvertString.split(" KiB")[0]) / 976.5625, 2)
        } else if (toConvertString.contains("MiB")) {
            Precision.round(Double.valueOf(toConvertString.split(" MiB")[0]) / 1.048576, 2)
        } else if (toConvertString.contains("GiB")) {
            Precision.round(Double.valueOf(toConvertString.split(" GiB")[0]) * 1073.741824, 2)
        } else if (toConvertString.contains("KB")) {
            Precision.round(Double.valueOf(toConvertString.split(" KB")[0]) / 1000, 2)
        } else if (toConvertString.contains("MB")) {
            Precision.round(Double.valueOf(toConvertString.split(" MB")[0]), 2)
        } else if (toConvertString.contains("GB")) {
            Precision.round(Double.valueOf(toConvertString.split(" GB")[0]) * 1000, 2)
        }

    }

    public static Double convertUnitStringToGB(String toConvertString) {

        if (toConvertString.contains("KiB")) {
            Precision.round(Double.valueOf(toConvertString.split(" KiB")[0]) * 1.024E-6, 4)
        } else if (toConvertString.contains("MiB")) {
            Precision.round(Double.valueOf(toConvertString.split(" MiB")[0]) * 0.001048576, 4)
        } else if (toConvertString.contains("GiB")) {
            Precision.round(Double.valueOf(toConvertString.split(" GiB")[0]) * 1.073741824, 4)
        } else if (toConvertString.contains("KB")) {
            Precision.round(Double.valueOf(toConvertString.split(" KB")[0]) / 1000 / 1000, 4)
        } else if (toConvertString.contains("MB")) {
            Precision.round(Double.valueOf(toConvertString.split(" MB")[0]) / 1000, 4)
        } else if (toConvertString.contains("GB")) {
            Precision.round(Double.valueOf(toConvertString.split(" GB")[0]), 4)
        }

    }
}

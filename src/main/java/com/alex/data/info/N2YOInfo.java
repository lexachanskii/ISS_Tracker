package com.alex.data.info;

public record N2YOInfo(
        long startUTC,
        String startLocal,
        String remaining,
        long endUTC,
        String endLocal,
        double maxEl,
        String direction,  // startAzCompass + "→" + endAzCompass
        int duration,
        double mag
) {

}

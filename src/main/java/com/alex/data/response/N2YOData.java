package com.alex.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record N2YOData(
        Info info,
        List<Pass> passes
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Info(
            int satid,
            String satname,
            int transactionscount,
            int passescount
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Pass(
            double startAz,
            String startAzCompass,
            double startEl,
            long startUTC,
            double maxAz,
            String maxAzCompass,
            double maxEl,
            long maxUTC,
            double endAz,
            String endAzCompass,
            double endEl,
            long endUTC,
            double mag,
            int duration,
            long startVisibility
    ) {}
}
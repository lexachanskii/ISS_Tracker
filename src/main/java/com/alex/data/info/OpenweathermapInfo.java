package com.alex.data.info;

public record OpenweathermapInfo(
        int clouds,
        String description,
        double temp,
        double windSpeed
) {}

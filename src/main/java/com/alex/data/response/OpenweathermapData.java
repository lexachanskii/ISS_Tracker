package com.alex.data.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenweathermapData(
        String cod,
        int cnt,
        List<WeatherSlot> list,
        City city
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WeatherSlot(
            long dt,
            Main main,
            List<Weather> weather,
            Clouds clouds,
            Wind wind,
            int visibility,
            String dt_txt
    ) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Main(
            double temp,
            double feels_like,
            double temp_min,
            double temp_max,
            int pressure,
            int humidity
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Weather(
            int id,
            String main,
            String description,
            String icon
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Clouds(int all) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Wind(
            double speed,
            int deg,
            double gust
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record City(
            int id,
            String name,
            Coord coord,
            String country,
            int timezone,
            long sunrise,
            long sunset
    ) {}

    public record Coord(double lat, double lon) {}
}

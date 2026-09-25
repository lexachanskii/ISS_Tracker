package com.alex.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseSunsetData(
        Results results,
        String status,
        String tzid
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Results(
            String sunrise,
            String sunset,
            String solar_noon,
            String day_length,
            String civil_twilight_begin,
            String civil_twilight_end,
            String nautical_twilight_begin,
            String nautical_twilight_end,
            String astronomical_twilight_begin,
            String astronomical_twilight_end
    ) {}
}

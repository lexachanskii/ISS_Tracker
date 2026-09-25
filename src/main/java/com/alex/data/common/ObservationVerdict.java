package com.alex.data.common;

import com.alex.data.info.N2YOInfo;
import com.alex.data.info.OpenweathermapInfo;
import com.alex.data.info.SunriseSunsetInfo;

public record ObservationVerdict(
        long id,
        java.sql.Timestamp timestamp,
        N2YOInfo pass,
        OpenweathermapInfo weather,
        SunriseSunsetInfo twilight,
        String verdict
) {}

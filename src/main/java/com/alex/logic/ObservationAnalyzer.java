package com.alex.logic;

import com.alex.data.DTO.DataDto;
import com.alex.data.common.ObservationVerdict;
import com.alex.data.info.N2YOInfo;
import com.alex.data.info.OpenweathermapInfo;
import com.alex.data.info.SunriseSunsetInfo;
import com.alex.data.response.N2YOData;
import com.alex.data.response.OpenweathermapData;
import com.alex.data.response.SunriseSunsetData;
import com.alex.parser.JsonParser;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.abs;

public class ObservationAnalyzer {
    N2YOInfo n2YOInfo;
    OpenweathermapInfo openweathermapInfo;
    SunriseSunsetInfo sunriseSunsetInfo;

    private String utcToLocal(long utc, int timezoneOffset) {
        return Instant.ofEpochSecond(utc)
                .atZone(ZoneOffset.ofTotalSeconds(timezoneOffset))
                .format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
    }

    private String remaining(long utc1, long utc2) {
        Duration until = Duration.ofSeconds(abs(utc1 - utc2));
        return String.format("%d:%02d:%02d", until.toHours(), until.toMinutesPart(), until.toSecondsPart());
    }

    private void dataToInfo(List<DataDto> data) throws IOException {
        N2YOData n2YOData = null;
        OpenweathermapData openweathermapData = null;
        SunriseSunsetData sunriseSunsetData = null;

        for (DataDto dataDto: data) {
            switch (dataDto.source()) {
                case "n2yo" -> n2YOData = JsonParser.deserializeDTO(dataDto.data(), N2YOData.class);
                case "openweathermap" -> openweathermapData = JsonParser.deserializeDTO(dataDto.data(), OpenweathermapData.class);
                case "sunrise-sunset" -> sunriseSunsetData = JsonParser.deserializeDTO(dataDto.data(), SunriseSunsetData.class);
                default -> throw new IllegalArgumentException("Unknown API type: " + dataDto.source());
            }
        }

        if (n2YOData == null || openweathermapData == null || sunriseSunsetData == null) {
            throw new IllegalStateException("Missing API data");
        }

        N2YOData.Pass bestPass = n2YOData.passes().get(0);

        n2YOInfo = new N2YOInfo(
                bestPass.startUTC(),
                utcToLocal(bestPass.startUTC(), openweathermapData.city().timezone()),
                remaining(bestPass.startUTC() ,Instant.now().getEpochSecond()),
                bestPass.endUTC(),
                utcToLocal(bestPass.endUTC(), openweathermapData.city().timezone()),
                bestPass.maxEl(),
                bestPass.startAzCompass() + "->" + bestPass.endAzCompass(),
                bestPass.duration(),
                bestPass.mag()
        );

        OpenweathermapData.WeatherSlot closestSlot = openweathermapData.list().stream()
                .min(Comparator.comparingLong(slot -> abs(slot.dt() - n2YOInfo.startUTC())))
                .orElseThrow(() -> new IllegalStateException("No weather data"));

        openweathermapInfo = new OpenweathermapInfo(
                closestSlot.clouds().all(),
                closestSlot.weather().getFirst().description(),
                closestSlot.main().temp(),
                closestSlot.wind().speed()
        );

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH);
        LocalTime civilEnd = LocalTime.parse(sunriseSunsetData.results().civil_twilight_end(), fmt).plusSeconds(openweathermapData.city().timezone());
        String civilEndLocal = civilEnd.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime astroEnd = LocalTime.parse(sunriseSunsetData.results().astronomical_twilight_end(), fmt).plusSeconds(openweathermapData.city().timezone());
        String astroEndLocal = astroEnd.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        sunriseSunsetInfo = new SunriseSunsetInfo(
                sunriseSunsetData.results().civil_twilight_end(),
                civilEndLocal,
                sunriseSunsetData.results().astronomical_twilight_end(),
                astroEndLocal
        );
    }

    private String calculateVerdict() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime passTime = LocalTime.parse(n2YOInfo.startLocal().substring(0, 8), fmt);
        LocalTime civilEnd = LocalTime.parse(sunriseSunsetInfo.civilEndLocal(), fmt);
        LocalTime astroEnd = LocalTime.parse(sunriseSunsetInfo.astroEndLocal(), fmt);

        boolean inWindow = passTime.isAfter(civilEnd) && passTime.isBefore(astroEnd)  // вечер
                || passTime.isBefore(civilEnd) && passTime.isAfter(astroEnd);  // утро

        boolean clearSky = openweathermapInfo.clouds() < 70;

        if (!inWindow && !clearSky) return "CLOUDY_WRONG_TIME";
        if (!inWindow)              return "WRONG_TIME";
        if (!clearSky)              return "CLOUDY";
        return "VISIBLE";
    }


    public ObservationVerdict getObservationVerdict(List<DataDto> data) throws IOException {
        dataToInfo(data);

        return new ObservationVerdict(
                1L,
                new java.sql.Timestamp(System.currentTimeMillis()),
                n2YOInfo,
                openweathermapInfo,
                sunriseSunsetInfo,
                calculateVerdict()
        );
    }
}

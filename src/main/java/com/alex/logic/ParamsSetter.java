package com.alex.logic;

import com.alex.data.common.ApiConfig;

import java.security.InvalidParameterException;
import java.util.*;

public class ParamsSetter {
    public static List<String> setN2YOPath(ApiConfig.ApiDefinition config, Scanner scanner) {

        if (!Objects.equals(config.name(), "n2yo")) {
            throw new InvalidParameterException("Current config is not for n2yo");
        }

        List<String> path = new ArrayList<>(config.extra_path());

        System.out.print("satellite (current: " + path.get(0) + ", enter to skip): ");
        String sat = scanner.nextLine().trim();
        if (!sat.isEmpty()) path.set(0, sat);

        System.out.print("lat (current: " + path.get(1) + ", enter to skip): ");
        String lat = scanner.nextLine().trim();
        if (!lat.isEmpty()) path.set(1, lat);

        System.out.print("lng (current: " + path.get(2) + ", enter to skip): ");
        String lng = scanner.nextLine().trim();
        if (!lng.isEmpty()) path.set(2, lng);

        System.out.print("days 1-10 (current: " + path.get(4) + ", enter to skip): ");
        String days = scanner.nextLine().trim();
        if (!days.isEmpty()) path.set(4, days);

        System.out.print("min visibility sec (current: " + path.get(5) + ", enter to skip): ");
        String minVis = scanner.nextLine().trim();
        if (!minVis.isEmpty()) path.set(5, minVis);

        return path;
    }

    public static Map<String, String> setWeatherParams(ApiConfig.ApiDefinition config, Scanner scanner) {
        if (!Objects.equals(config.name(), "openweathermap")) {
            throw new InvalidParameterException("Current config is not for openweathermap");
        }

        Map<String, String> params = new HashMap<>(config.params());

        System.out.print("lat (current: " + params.get("lat") + ", enter to skip): ");
        String lat = scanner.nextLine().trim();
        if (!lat.isEmpty()) params.put("lat", lat);

        System.out.print("lon (current: " + params.get("lon") + ", enter to skip): ");
        String lon = scanner.nextLine().trim();
        if (!lon.isEmpty()) params.put("lon", lon);

        System.out.print("cnt (current: " + params.get("cnt") + ", enter to skip): ");
        String cnt = scanner.nextLine().trim();
        if (!cnt.isEmpty()) params.put("cnt", cnt);

        return params;
    }

    public static Map<String, String> setSunriseParams(ApiConfig.ApiDefinition config, Scanner scanner) {
        if (!Objects.equals(config.name(), "sunrise-sunset")) {
            throw new InvalidParameterException("Current config is not for sunrise-sunset");
        }

        Map<String, String> params = new HashMap<>(config.params());

        System.out.print("lat (current: " + params.get("lat") + ", enter to skip): ");
        String lat = scanner.nextLine().trim();
        if (!lat.isEmpty()) params.put("lat", lat);

        System.out.print("lng (current: " + params.get("lng") + ", enter to skip): ");
        String lng = scanner.nextLine().trim();
        if (!lng.isEmpty()) params.put("lng", lng);

        return params;
    }

    public static ApiConfig mergeConfig(ApiConfig original, Map<String, List<String>> customPaths, Map<String, Map<String, String>> customParams) {
        List<ApiConfig.ApiDefinition> merged = original.apis().stream()
                .map(def -> new ApiConfig.ApiDefinition(
                        def.name(),
                        def.baseUrl(),
                        customPaths.getOrDefault(def.name(), def.extra_path()),
                        customParams.getOrDefault(def.name(), def.params())
                ))
                .toList();

        return new ApiConfig(merged);
    }
}

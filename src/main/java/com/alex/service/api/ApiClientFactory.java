package com.alex.service.api;

import com.alex.data.common.ApiConfig;

public class ApiClientFactory {

    public static ApiClient create(ApiConfig.ApiDefinition config) {
        return switch (config.name()) {
            case "n2yo" -> new N2YOApiClient(config);
            case "openweathermap" -> new OpenweathermapApiClient(config);
            case "sunrise-sunset" -> new SunriseSunsetApiClient(config);
            default -> throw new IllegalArgumentException("Unknown API type: " + config.name());
        };
    }
}

package com.alex.service;

import com.alex.data.DTO.DataDto;
import com.alex.data.common.ApiConfig;
import com.alex.service.api.ApiClient;
import com.alex.service.api.AbstractApiClient;
import com.alex.service.api.ApiClientFactory;
import com.alex.service.api.N2YOApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Agregation {
    private final List<ApiClient> clients = new ArrayList<ApiClient>();;

    public Agregation(List<ApiConfig.ApiDefinition> configs) {
        for (ApiConfig.ApiDefinition config: configs) {
            clients.add(ApiClientFactory.create(config));
        }
    }

    public List<DataDto> collectALL() throws IOException {
        List<DataDto> responds = new ArrayList<>();

        for (ApiClient client : clients) {
            responds.add(client.fetch());
        }
        return responds;
    }
}

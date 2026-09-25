package com.alex.service.api;

import com.alex.data.DTO.DataDto;
import com.alex.data.common.ApiConfig;
import com.alex.service.httpProvider.HttpClientProvider;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class N2YOApiClient extends AbstractApiClient{
    public N2YOApiClient(ApiConfig.ApiDefinition config) {
        super(config);
    }

    @Override
    public DataDto fetch() throws IOException {
        try (Response response = HttpClientProvider.CLIENT.newCall(buldRequest()).execute()) {
            if (response.body() != null) {
                return parseResponse(response);
            };
            return null;
        }
    }

}

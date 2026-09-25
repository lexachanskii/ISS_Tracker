package com.alex.service.api;

import com.alex.data.DTO.DataDto;
import com.alex.data.common.ApiConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class AbstractApiClient implements ApiClient{
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ApiConfig.ApiDefinition config;

    public AbstractApiClient(ApiConfig.ApiDefinition config) {
        this.config = config;
    }

    @Override
    public Request buldRequest() throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(config.baseUrl()).newBuilder();

        if (config.extra_path() != null) {
            config.extra_path().forEach(urlBuilder::addPathSegment);
        }

        if (config.params() != null) {
            config.params().forEach(urlBuilder::addQueryParameter);
        }

        HttpUrl url = urlBuilder.build();

        System.out.println(url);

        return new Request.Builder().url(url).get().build();

    }

    @Override
    public DataDto parseResponse(Response response) throws IOException {
            if (response.body() != null )  {
                String body = response.body().string();
                JsonNode dataNode = MAPPER.readTree(body);

                return new DataDto(1L, this.name(),new java.sql.Timestamp(System.currentTimeMillis()),dataNode );
            }
            return null;
    }

    @Override
    public String name() {
        return config.name();
    }
}

package com.alex.service.api;

import com.alex.data.DTO.DataDto;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public interface ApiClient {

    Request buldRequest() throws IOException;
    DataDto parseResponse(Response response) throws IOException;
    DataDto fetch() throws IOException;

    String name();
}

package com.alex.data.common;

import java.util.List;
import java.util.Map;

public record ApiConfig(List<ApiDefinition> apis) {
    public record ApiDefinition(
            String name,
            String baseUrl,
            List<String> extra_path,
            Map<String, String> params
    ) {}
}
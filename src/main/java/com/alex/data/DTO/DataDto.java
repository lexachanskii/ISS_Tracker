package com.alex.data.DTO;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Timestamp;

public record DataDto(Long id, String source, Timestamp timestamp, JsonNode data) { }

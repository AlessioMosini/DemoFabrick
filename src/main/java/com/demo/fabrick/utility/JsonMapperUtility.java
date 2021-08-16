package com.demo.fabrick.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class JsonMapperUtility {

    public <T> T convertJsonToObject(String json, TypeReference<T> type) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, type);
    }

    public String convertObjectToJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}

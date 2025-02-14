package com.fun.LogicApp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
@Component
public abstract class LogicUnit<INPUT, OUTPUT> implements Runnable {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Holds the INPUT and OUTPUT data objects
    private INPUT input;
    private OUTPUT output;

    // Get in the input class for deserialization
    public abstract Class<INPUT> getInputClass();

    // Deserializes the JSON input and initializes LogicUnit if it is needed
    public void init(String jsonInput) throws Exception {
        JsonNode inputJsonObject = objectMapper.readTree(jsonInput);

        // Go through all the JsonNode fields and evaluate all input fields that are function for SpEL
        
        input = objectMapper.treeToValue(inputJsonObject, getInputClass());
    }

    private static class A {
        private int a;
    }
}

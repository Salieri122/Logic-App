package com.fun.LogicApp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import lombok.Data;

@Data
public abstract class LogicUnit<INPUT, OUTPUT> implements Runnable {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Holds the INPUT and OUTPUT data objects
    protected INPUT input;
    protected OUTPUT output;

    private JsonNode newJsonNode;

    // Get in the input class for deserialization
    public abstract Class<INPUT> getInputClass();

    protected abstract OUTPUT initOutput();

    // Deserializes the JSON input and initializes LogicUnit if it is needed
    public void init(String jsonInput) throws Exception {
        JsonNode inputJsonObject = objectMapper.readTree(jsonInput);

        // Go through all the JsonNode fields and evaluate all input fields that are function for SpEL
        inputJsonObject.fields().forEachRemaining(entry -> {
            if(!entry.getValue().isObject()) {
                return;
            }

            // Parse funtion on a field
            try {
                JsonEvalObject jsonEvalObject = objectMapper.treeToValue(entry.getValue(), JsonEvalObject.class);

                // TODO: Evaluate formula !!!
                String value = jsonEvalObject.getValue();

                // Initialize new node
                JsonNode newJsonNode = createNewJsonNode(jsonEvalObject.getType(), value);
                entry.setValue(newJsonNode);
            } catch (JsonProcessingException | IllegalArgumentException e) {
                // it means that it is a inner object so we let it be, for now !!!
                return;
            }
        });

        
        input = objectMapper.treeToValue(inputJsonObject, getInputClass());
    }

    public void execute() {
        output = initOutput();
        run();
    }

    private JsonNode createNewJsonNode(JsonNodeType type, String value) {
        return switch (type) {
            case STRING -> objectMapper.getNodeFactory().textNode(value);
            case NUMBER -> objectMapper.getNodeFactory().numberNode(Double.parseDouble(value));
            case BOOLEAN -> objectMapper.getNodeFactory().booleanNode(Boolean.parseBoolean(value));
            case NULL -> objectMapper.getNodeFactory().nullNode();
            default -> objectMapper.getNodeFactory().nullNode();
        };
    }

    @Data
    private static class JsonEvalObject {

        private JsonNodeType type;
        private String value;
    }
}

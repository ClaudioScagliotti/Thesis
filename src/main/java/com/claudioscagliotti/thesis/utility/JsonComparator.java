package com.claudioscagliotti.thesis.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonComparator {

    private JsonComparator() {
        throw new UnsupportedOperationException("Utility class");
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Compares two JSON strings and returns true if they are equivalent, false otherwise.
     *
     * @param json1 The first JSON string.
     * @param json2 The second JSON string.
     * @return true if the JSON strings are equal, false otherwise.
     * @throws JsonProcessingException if any of the JSON strings is invalid.
     */
    public static boolean compareJson(String json1, String json2) throws JsonProcessingException {
        JsonNode tree1 = objectMapper.readTree(json1);
        JsonNode tree2 = objectMapper.readTree(json2);
        return tree1.equals(tree2);
    }

    /**
     * Pretty prints a JSON string.
     *
     * @param json The JSON string to format.
     * @return The formatted JSON string.
     * @throws JsonProcessingException if the JSON string is invalid.
     */
    public static String prettyPrintJson(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    }

    /**
     * Converts a Java object to a JSON string.
     *
     * @param object The Java object to convert.
     * @return The JSON string.
     * @throws JsonProcessingException if the conversion fails.
     */
    public static String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Converts a JSON string to a Java object of the specified type.
     *
     * @param json  The JSON string to convert.
     * @param clazz The class of the desired object.
     * @param <T>   The type of the desired object.
     * @return The converted Java object.
     * @throws JsonProcessingException if the conversion fails.
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
}

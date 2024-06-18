package com.claudioscagliotti.thesis.utility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonComparator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Compara due stringhe JSON e ritorna true se sono equivalenti, false altrimenti.
     *
     * @param json1 La prima stringa JSON.
     * @param json2 La seconda stringa JSON.
     * @return true se i JSON sono uguali, false altrimenti.
     * @throws JsonProcessingException se uno dei JSON non è valido.
     */
    public static boolean compareJson(String json1, String json2) throws JsonProcessingException {
        JsonNode tree1 = objectMapper.readTree(json1);
        JsonNode tree2 = objectMapper.readTree(json2);
        return tree1.equals(tree2);
    }

    /**
     * Pretty print di una stringa JSON.
     *
     * @param json La stringa JSON da formattare.
     * @return La stringa JSON formattata.
     * @throws JsonProcessingException se il JSON non è valido.
     */
    public static String prettyPrintJson(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    }

    /**
     * Converte un oggetto Java in una stringa JSON.
     *
     * @param object L'oggetto Java da convertire.
     * @return La stringa JSON.
     * @throws JsonProcessingException se la conversione fallisce.
     */
    public static String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Converte una stringa JSON in un oggetto Java del tipo specificato.
     *
     * @param json La stringa JSON da convertire.
     * @param clazz La classe dell'oggetto desiderato.
     * @param <T> Il tipo dell'oggetto desiderato.
     * @return L'oggetto Java convertito.
     * @throws JsonProcessingException se la conversione fallisce.
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
}

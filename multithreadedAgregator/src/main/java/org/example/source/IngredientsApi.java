package org.example.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.httpClient.HttpClientWrapper;
import org.example.uniformModel.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientsApi implements CommonInterface {
    private final HttpClientWrapper httpClient;
    private final ObjectMapper objectMapper;
    private final String url;

    public IngredientsApi(HttpClientWrapper httpClient, String url) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.url = url;
    }

    @Override
    public String getSourceName() {
        return "exactcup";
    }

    @Override
    public List<CommonModel> fetchData() throws Exception {
        String jsonResponse = httpClient.get(url);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        List<CommonModel> records = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("name", rootNode.path("name").asText());
        dataMap.put("category", rootNode.path("category_name").asText());
        dataMap.put("grams_per_cup", rootNode.path("grams_per_us_cup").asDouble());
        dataMap.put("ounces_per_cup", rootNode.path("ounces_per_us_cup").asDouble());

        JsonNode conversionsNode = rootNode.path("conversions");

        List<Map<String, Object>> cupsToGramsList = new ArrayList<>();
        JsonNode cupsArray = conversionsNode.path("cups_to_grams");
        if (cupsArray.isArray()) {
            for (JsonNode item : cupsArray) {
                Map<String, Object> conv = new HashMap<>();
                conv.put("cups", item.path("cups").asText());
                conv.put("grams", item.path("grams").asDouble());
                cupsToGramsList.add(conv);
            }
        }
        dataMap.put("cups_to_grams", cupsToGramsList);

        List<Map<String, Object>> tbspToGramsList = new ArrayList<>();
        JsonNode tbspArray = conversionsNode.path("tablespoons_to_grams");
        if (tbspArray.isArray()) {
            for (JsonNode item : tbspArray) {
                Map<String, Object> conv = new HashMap<>();
                conv.put("tablespoons", item.path("tablespoons").asInt());
                conv.put("grams", item.path("grams").asDouble());
                tbspToGramsList.add(conv);
            }
        }
        dataMap.put("tablespoons_to_grams", tbspToGramsList);

        List<Map<String, Object>> gramsToCupsList = new ArrayList<>();
        JsonNode gramsArray = conversionsNode.path("grams_to_cups");
        if (gramsArray.isArray()) {
            for (JsonNode item : gramsArray) {
                Map<String, Object> conv = new HashMap<>();
                conv.put("grams", item.path("grams").asInt());
                conv.put("cups", item.path("cups").asDouble());
                gramsToCupsList.add(conv);
            }
        }
        dataMap.put("grams_to_cups", gramsToCupsList);

        CommonModel model = new CommonModel(
                getSourceName(),
                "ingredient",
                System.currentTimeMillis(),
                dataMap
        );
        records.add(model);

        return records;
    }
}
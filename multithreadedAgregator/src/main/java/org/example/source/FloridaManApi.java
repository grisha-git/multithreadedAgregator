package org.example.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.httpClient.HttpClientWrapper;
import org.example.uniformModel.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloridaManApi implements CommonInterface {
    private final HttpClientWrapper httpClient;
    private final ObjectMapper objectMapper;
    private final String url;

    public FloridaManApi(HttpClientWrapper httpClient, String url) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.url = url;
    }

    @Override
    public String getSourceName() {
        return "florida-man";
    }

    @Override
    public List<CommonModel> fetchData() throws Exception {
        String jsonResponse = httpClient.get(url);

        JsonNode rootArray = objectMapper.readTree(jsonResponse);

        List<CommonModel> records = new ArrayList<>();

        if (rootArray.isArray()) {
            for (JsonNode newsNode : rootArray) {
                Map<String, Object> dataMap = new HashMap<>();

                dataMap.put("title", newsNode.path("title").asText());
                dataMap.put("url", newsNode.path("url").asText());
                dataMap.put("source", newsNode.path("source").asText());
                dataMap.put("date", newsNode.path("date").asText());

                List<String> keywordsList = new ArrayList<>();
                JsonNode keywordsNode = newsNode.path("keywords");
                if (keywordsNode.isArray()) {
                    for (JsonNode kw : keywordsNode) {
                        keywordsList.add(kw.asText());
                    }
                }
                dataMap.put("keywords", keywordsList);

                CommonModel model = new CommonModel(
                        getSourceName(),
                        "news",
                        System.currentTimeMillis(),
                        dataMap
                );
                records.add(model);
            }
        }
        return records;
    }
}
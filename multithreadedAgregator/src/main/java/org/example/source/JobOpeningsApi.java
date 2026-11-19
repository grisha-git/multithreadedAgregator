package org.example.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cli.HttpClientWrapper;
import org.example.uniformModel.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobOpeningsApi implements CommonInterface{
    private final HttpClientWrapper httpClient;
    private final ObjectMapper objectMapper;
    private final String url;

    public JobOpeningsApi(HttpClientWrapper httpClient, String url) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper(); // Парсер JSON
        this.url = url;
    }

    @Override
    public String getSourceName() {
        return "job_opening";
    }

    @Override
    public List<CommonModel> fetchData() throws Exception {
        String jsonResponse = httpClient.get(url);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode dataArray = rootNode.get("data");

        List<CommonModel> records = new ArrayList<>();

        if (dataArray != null && dataArray.isArray()) {
            for (JsonNode jobNode : dataArray) {
                Map<String, Object> dataMap = new HashMap<>();

                dataMap.put("title", jobNode.path("title").asText());
                dataMap.put("company", jobNode.path("company_name").asText());
                dataMap.put("location", jobNode.path("location").asText());
                dataMap.put("remote", jobNode.path("remote").asBoolean());
                dataMap.put("created_at", jobNode.path("created_at").asLong());

                CommonModel model = new CommonModel(
                        getSourceName(),
                        "job",
                        System.currentTimeMillis(),
                        dataMap
                );

                records.add(model);
            }
        }
        return records;
    }

}

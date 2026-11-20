package org.example.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.httpClient.HttpClientWrapper;
import org.example.uniformModel.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobOpeningsApi implements CommonInterface {
    private final HttpClientWrapper httpClient;
    private final ObjectMapper objectMapper;
    private final String url;

    public JobOpeningsApi(HttpClientWrapper httpClient, String url) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.url = url;
    }

    @Override
    public String getSourceName() {
        return "remotive"; // Имя API
    }

    @Override
    public List<CommonModel> fetchData() throws Exception {
        String jsonResponse = httpClient.get(url);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        JsonNode jobsArray = rootNode.get("jobs");

        List<CommonModel> records = new ArrayList<>();

        if (jobsArray != null && jobsArray.isArray()) {
            for (JsonNode jobNode : jobsArray) {
                Map<String, Object> dataMap = new HashMap<>();

                dataMap.put("title", jobNode.path("title").asText());
                dataMap.put("company", jobNode.path("company_name").asText());
                dataMap.put("category", jobNode.path("category").asText());
                dataMap.put("job_type", jobNode.path("job_type").asText());
                dataMap.put("location", jobNode.path("candidate_required_location").asText());
                dataMap.put("salary", jobNode.path("salary").asText());
                dataMap.put("publication_date", jobNode.path("publication_date").asText());

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
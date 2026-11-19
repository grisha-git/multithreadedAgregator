package org.example;

import org.example.httpClient.HttpClientWrapper;
import org.example.uniformModel.CommonModel;
import org.example.source.JobOpeningsApi;
import org.example.source.CommonInterface;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            HttpClientWrapper httpClient = new HttpClientWrapper();
            List<CommonInterface> sources = new ArrayList<>();
            List<CommonModel> allData = new ArrayList<>();

            sources.add(new JobOpeningsApi(httpClient, "https://arbeitnow.com/api/job-board-api?page=1"));

            for (CommonInterface source : sources) {
                List<CommonModel> fetched = source.fetchData();
                allData.addAll(fetched);
            }

            if (!allData.isEmpty()) {
                System.out.println("First record data: " + allData.get(0).getData());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("URL error: " + e.getMessage());
        } catch (java.io.IOException | InterruptedException e) {
            System.err.println("Network error or timeout: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}
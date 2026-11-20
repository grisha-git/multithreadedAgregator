package org.example;

import org.example.cli.CLIOptions;
import org.example.cli.CommandLineParser;
import org.example.httpClient.HttpClientWrapper;
import org.example.uniformModel.CommonModel;
import org.example.source.JobOpeningsApi;
import org.example.source.CommonInterface;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CLIOptions options = CommandLineParser.parse(args);

            HttpClientWrapper httpClient = new HttpClientWrapper();
            List<CommonInterface> sources = new ArrayList<>();
            List<CommonModel> allData = new ArrayList<>();

            if (options.getApis().contains("job")) {
                sources.add(new JobOpeningsApi(httpClient, "https://remotive.com/api/remote-jobs?limit=10"));
            }
            if (sources.isEmpty()) {
                System.out.println("No api parameter");
                return;
            }

            for (CommonInterface source : sources) {
                List<CommonModel> fetched = source.fetchData();
                allData.addAll(fetched);
            }

            if (!allData.isEmpty()) {
                System.out.println("First record data: " + allData.get(0).getData());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("URL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
            e.printStackTrace(); // Добавь для отладки, чтобы видеть, где упало
        }
    }
}
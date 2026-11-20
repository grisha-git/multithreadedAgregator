package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.cli.CLIOptions;
import org.example.cli.CommandLineParser;
import org.example.httpClient.HttpClientWrapper;
import org.example.storage.JsonStorage;
import org.example.uniformModel.CommonModel;
import org.example.source.JobOpeningsApi;
import org.example.source.IngredientsApi;
import org.example.source.FloridaManApi;
import org.example.source.CommonInterface;
import org.example.storage.Storage;
import org.example.storage.CsvStorage;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CLIOptions options = CommandLineParser.parse(args);

            if (options.isRead()) {
                Storage storage;
                String filename;
                if (options.getFormat().equals("csv")) {
                    storage = new CsvStorage();
                    filename = "results.csv";
                } else {
                    storage = new JsonStorage();
                    filename = "results.json";
                }

                List<CommonModel> data = storage.read(filename);
                System.out.println("Прочитано записей: " + data.size());

                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                System.out.println(mapper.writeValueAsString(data));
                return;
            }

            HttpClientWrapper httpClient = new HttpClientWrapper();
            List<CommonInterface> sources = new ArrayList<>();
            List<CommonModel> allData = new ArrayList<>();

            if (options.getApis().contains("job")) {
                sources.add(new JobOpeningsApi(httpClient, "https://remotive.com/api/remote-jobs?limit=10"));
            }
            if (options.getApis().contains("ingredient")) {
                sources.add(new IngredientsApi(httpClient, "https://exactcup.github.io/api/v1/ingredients/honey.json"));
            }
            if (options.getApis().contains("news")) {
                sources.add(new FloridaManApi(httpClient, "https://juliayxhuang.github.io/florida-man-api/api/04/23.json"));
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
                System.out.println("Всего записей: " + allData.size());

                if (!allData.isEmpty()) {
                    System.out.println("Всего записей: " + allData.size());

                    Storage storage;
                    String filename;
                    boolean appendMode = options.getMode().equals("append");

                    if (options.getFormat().equals("csv")) {
                        storage = new CsvStorage();
                        filename = "results.csv";
                    } else {
                        storage = new JsonStorage();
                        filename = "results.json";
                    }

                    storage.save(allData, filename, appendMode);
                    System.out.println("Данные сохранены в " + filename + " (Режим: " + (appendMode ? "дозапись" : "создать") + ")");

                }
            }

            if (!allData.isEmpty()) {
                System.out.println("First record data: " + allData.get(0).getData());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("URL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
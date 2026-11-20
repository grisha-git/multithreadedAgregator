package org.example.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.example.uniformModel.CommonModel;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvStorage implements Storage {
    private final ObjectMapper objectMapper;

    public CsvStorage() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(List<CommonModel> data, String filename, boolean append) throws Exception {
        boolean fileExists = new java.io.File(filename).exists() && new java.io.File(filename).length() > 0;

        boolean writeHeader = !(append && fileExists);

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("source", "category", "timestamp", "data")
                .setSkipHeaderRecord(!writeHeader)
                .build();

        try (Writer writer = new FileWriter(filename, append);
             CSVPrinter printer = new CSVPrinter(writer, format)) {

            for (CommonModel model : data) {
                String dataAsJson = objectMapper.writeValueAsString(model.getData());

                printer.printRecord(
                        model.getSource(),
                        model.getCategory(),
                        model.getTimestamp(),
                        dataAsJson
                );
            }
        }
    }

    @Override
    public List<CommonModel> read(String filename) throws Exception {
        List<CommonModel> result = new ArrayList<>();

        try (Reader reader = new FileReader(filename);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                     .setHeader("source", "category", "timestamp", "data")
                     .setSkipHeaderRecord(true)
                     .build())) {

            for (CSVRecord record : parser) {
                CommonModel model = new CommonModel();
                model.setSource(record.get("source"));
                model.setCategory(record.get("category"));
                model.setTimestamp(Long.parseLong(record.get("timestamp")));

                String dataJson = record.get("data");
                Map<String, Object> dataMap = objectMapper.readValue(dataJson, new TypeReference<Map<String, Object>>() {});
                model.setData(dataMap);

                result.add(model);
            }
        }
        return result;
    }
}
package org.example.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.uniformModel.CommonModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage implements Storage {
    private final ObjectMapper objectMapper;

    public JsonStorage() {
        this.objectMapper = new ObjectMapper();
        // Красивый вывод с отступами (чтобы файл читался глазами)
        this.objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void save(List<CommonModel> data, String filename, boolean append) throws Exception {
        File file = new File(filename);
        List<CommonModel> finalData = new ArrayList<>();

        // Если режим "дозапись" и файл существует, читаем старые данные
        if (append && file.exists()) {
            finalData.addAll(read(filename));
        }

        // Добавляем новые данные
        finalData.addAll(data);

        // Записываем всё обратно в файл
        objectMapper.writeValue(file, finalData);
    }

    @Override
    public List<CommonModel> read(String filename) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        // Читаем список объектов из файла
        return objectMapper.readValue(file, new TypeReference<List<CommonModel>>() {});
    }
}
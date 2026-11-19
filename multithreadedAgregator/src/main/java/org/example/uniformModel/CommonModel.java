package org.example.uniformModel;

import java.util.HashMap;
import java.util.Map;

public class CommonModel {
    private String source;
    private String category;
    private long timestamp;
    private Map<String, Object> data = new HashMap<>();

    public CommonModel() {}
    public CommonModel(String source, String category, long timestamp, Map<String, Object> data) {
        this.source = source;
        this.category = category;
        this.timestamp = timestamp;
        this.data = data;
    }
}

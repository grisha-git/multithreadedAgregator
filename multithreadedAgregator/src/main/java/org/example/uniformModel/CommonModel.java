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

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}

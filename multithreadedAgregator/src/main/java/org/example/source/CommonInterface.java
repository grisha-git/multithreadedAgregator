package org.example.source;

import org.example.uniformModel.CommonModel;

import java.util.List;

public interface CommonInterface {
    public String getSourceName();
    public List<CommonModel> fetchData() throws Exception;
}

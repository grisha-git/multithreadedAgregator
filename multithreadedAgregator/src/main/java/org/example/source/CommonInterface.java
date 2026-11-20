package org.example.source;

import org.example.uniformModel.CommonModel;

import java.util.List;

public interface CommonInterface {
    String getSourceName();
    List<CommonModel> fetchData() throws Exception;
}

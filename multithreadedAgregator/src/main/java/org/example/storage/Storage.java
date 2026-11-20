package org.example.storage;

import org.example.uniformModel.CommonModel;
import java.util.List;

public interface Storage {
    void save(List<CommonModel> data, String filename, boolean append) throws Exception;
    List<CommonModel> read(String filename) throws Exception;
}
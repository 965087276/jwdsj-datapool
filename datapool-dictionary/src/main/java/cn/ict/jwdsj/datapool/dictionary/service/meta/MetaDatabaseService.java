package cn.ict.jwdsj.datapool.dictionary.service.meta;

import java.util.List;

public interface MetaDatabaseService {
    boolean exists(String database);

    List<String> listDatabasesNotAdd();
}

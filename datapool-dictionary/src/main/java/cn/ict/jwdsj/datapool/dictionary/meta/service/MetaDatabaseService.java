package cn.ict.jwdsj.datapool.dictionary.meta.service;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;

import java.util.List;

public interface MetaDatabaseService {
    boolean exists(String database);

    List<String> listDatabasesNotAdd();
}

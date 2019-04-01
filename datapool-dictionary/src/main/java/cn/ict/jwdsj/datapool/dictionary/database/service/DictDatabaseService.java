package cn.ict.jwdsj.datapool.dictionary.database.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictDatabaseService {
    void save(DictDatabase dictDatabase);

    @Transactional
    void saveAll(List<DictDatabase> dictDatabases);

    boolean exists(String enDatabase);

    DictDatabase findByEnDatabase(String enDatabase);

}

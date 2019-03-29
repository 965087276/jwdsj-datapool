package cn.ict.jwdsj.datapool.dictionary.service.dict;

import cn.ict.jwdsj.datapool.dictionary.entity.dict.DictDatabase;

import java.util.List;

public interface DictDatabaseService {
    void save(DictDatabase dictDatabase);
    void saveAll(List<DictDatabase> dictDatabases);
}

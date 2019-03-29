package cn.ict.jwdsj.datapool.dictionary.service.dict.impl;

import cn.ict.jwdsj.datapool.dictionary.entity.dict.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.repository.dict.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.service.dict.DictDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictDatabaseServiceImpl implements DictDatabaseService {

    @Autowired
    private DictDatabaseRepo dictDatabaseRepo;

    @Override
    public void save(DictDatabase dictDatabase) {
        dictDatabaseRepo.save(dictDatabase);
    }

    @Override
    public void saveAll(List<DictDatabase> dictDatabases) {
        dictDatabaseRepo.saveAll(dictDatabases);
    }
}

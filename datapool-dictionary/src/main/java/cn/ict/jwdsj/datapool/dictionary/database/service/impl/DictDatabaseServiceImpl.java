package cn.ict.jwdsj.datapool.dictionary.database.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.repo.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
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

    @Override
    public boolean exists(String enDatabase) {
        return dictDatabaseRepo.existsByEnDatabase(enDatabase);
    }

    @Override
    public DictDatabase findByEnDatabase(String enDatabase) {
        return dictDatabaseRepo.findByEnDatabase(enDatabase);
    }


}

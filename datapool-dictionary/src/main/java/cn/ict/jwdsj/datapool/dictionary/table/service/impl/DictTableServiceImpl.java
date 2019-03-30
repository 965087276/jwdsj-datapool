package cn.ict.jwdsj.datapool.dictionary.table.service.impl;

import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.table.entity.DictTable;
import cn.ict.jwdsj.datapool.dictionary.table.repo.DictTableRepo;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictTableServiceImpl implements DictTableService {
    @Autowired
    private DictTableRepo dictTableRepo;

    @Override
    public void save(DictTable dictTable) {
        dictTableRepo.save(dictTable);
    }

    @Override
    public void saveAll(List<DictTable> dictTables) {
        dictTableRepo.saveAll(dictTables);
    }

    @Override
    public List<DictTable> listByDictDatabase(DictDatabase dictDatabase) {
        return dictTableRepo.findByDictDatabase(dictDatabase);
    }

    @Override
    public int countByDictDatabaseAndEnTableIn(DictDatabase dictDatabase, List<String> enTable) {
        return dictTableRepo.countByDictDatabaseAndEnTableIn(dictDatabase, enTable);
    }
}

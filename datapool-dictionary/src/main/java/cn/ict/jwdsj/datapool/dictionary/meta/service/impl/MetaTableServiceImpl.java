package cn.ict.jwdsj.datapool.dictionary.meta.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaTableRepo;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaTableService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetaTableServiceImpl implements MetaTableService {

    @Autowired private MetaTableRepo metaTableRepo;
    @Autowired private DictDatabaseService dictDatabaseService;
    @Autowired private DictTableService dictTableService;

    @Override
    public List<MetaTable> listByDatabase(String database) {
        return metaTableRepo.findByDatabase(database);
    }

    @Override
    public List<String> listTablesNotAdd(long databaseId) {
        DictDatabase dictDatabase = dictDatabaseService.findById(databaseId);

        Set<String> metaTables = this.listByDatabase(dictDatabase.getEnDatabase()).stream().map(MetaTable::getTable).collect(Collectors.toSet());
        List<String> dictTables = dictTableService.listEnTablesByDictDatabase(dictDatabase);
        metaTables.removeAll(dictTables);

        return metaTables.stream().collect(Collectors.toList());

    }
}

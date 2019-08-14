package cn.ict.jwdsj.datapool.dictionary.service.meta.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.secondary.meta.MetaTableMapper;
import cn.ict.jwdsj.datapool.dictionary.dao.repo.database.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.service.meta.MetaTableService;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.table.DictTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetaTableServiceImpl implements MetaTableService {

    @Autowired private MetaTableMapper metaTableMapper;
    @Autowired private DictDatabaseRepo dictDatabaseRepo;
    @Autowired private DictTableMapper dictTableMapper;

    @Override
    public List<MetaTable> listByDatabase(String database) {
        return metaTableMapper.listByDatabase(database)
                .stream()
                .map(table -> new MetaTable(database, table))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> listTablesNotAdd(long databaseId) {

        DictDatabase dictDatabase = dictDatabaseRepo.findById(databaseId).get();

        Set<String> metaTables = this.listByDatabase(dictDatabase.getEnDatabase()).stream().map(MetaTable::getTable).collect(Collectors.toSet());
        List<String> dictTables = dictTableMapper.listEnTableByDatabaseId(dictDatabase.getId());
        metaTables.removeAll(dictTables);

        return metaTables.stream().collect(Collectors.toList());

    }
}

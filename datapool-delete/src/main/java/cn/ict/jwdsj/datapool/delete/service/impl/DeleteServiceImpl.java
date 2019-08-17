package cn.ict.jwdsj.datapool.delete.service.impl;

import cn.ict.jwdsj.datapool.delete.mapper.primary.DictMapper;
import cn.ict.jwdsj.datapool.delete.mapper.primary.IndexManageMapper;
import cn.ict.jwdsj.datapool.delete.mapper.primary.StatsMapper;
import cn.ict.jwdsj.datapool.delete.mapper.secondary.DataPoolMapper;
import cn.ict.jwdsj.datapool.delete.service.DeleteService;
import cn.ict.jwdsj.datapool.delete.service.ElasticRestService;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeleteServiceImpl implements DeleteService {

    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private IndexManageMapper indexManageMapper;
    @Autowired
    private StatsMapper statsMapper;
    @Autowired
    private DataPoolMapper poolMapper;
    @Autowired
    private ElasticRestService restService;

    @Override
    @Async
    public void scheduledDelete() throws IOException {
        List<String> dictDatabases = dictMapper.listEnDatabase();
        Set<String> poolDatabases = poolMapper.listDatabase().stream().map(Strings::toLowerCase).collect(Collectors.toSet());

        for (String dictDatabase : dictDatabases) {
            // 字典中的某个库在数据池中不存在，则删除所有与该库有关的业务数据
            if (!poolDatabases.contains(Strings.toLowerCase(dictDatabase))) {
                long databaseId = dictMapper.findIdByEnDatabase(dictDatabase);
                this.deleteByDatabaseId(databaseId);
            }
            else {
                List<String> dictTables = dictMapper.listEnTableByEnDatabase(dictDatabase);
                Set<String> poolTables = poolMapper.listTableByDatabase(dictDatabase).stream().map(Strings::toLowerCase).collect(Collectors.toSet());
                for (String dictTable : dictTables) {
                    // 字典中的某个表在数据池中不存在，则删除所有与该表有关的业务数据
                    if (!poolTables.contains(Strings.toLowerCase(dictTable))) {
                        long tableId = dictMapper.findIdByEnDatabaseAndEnTable(dictDatabase, dictTable);
                        this.deleteByTableId(tableId);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, value = "primaryTransactionManager")
    public void deleteByDatabaseId(long databaseId) throws IOException {

        // 获取该库下已经同步数据至搜索引擎的表的id
        List<Long> tableIds = indexManageMapper.getMappingTableIdByDatabaseId(databaseId);

        for (Long tableId : tableIds) {
            String indexName = indexManageMapper.getIndexNameByTableId(tableId);
            restService.deleteDocsByTableId(indexName, tableId);
            restService.deleteAliasByIndexNameAndTableId(indexName, tableId);
        }

        dictMapper.deleteDictDatabaseById(databaseId);
        dictMapper.deleteDictTableByDatabaseId(databaseId);
        dictMapper.deleteDictColumnByDatabaseId(databaseId);

        statsMapper.deleteStatsDatabaseByDatabaseId(databaseId);
        statsMapper.deleteStatsTableByDatabaseId(databaseId);
        statsMapper.deleteStatsColumnByDatabaseId(databaseId);

        indexManageMapper.deleteMappingColumnByDatabaseId(databaseId);
        indexManageMapper.deleteMappingTableByDatabaseId(databaseId);
        indexManageMapper.deleteSeTableByDatabaseId(databaseId);

    }

    @Override
    @Transactional(rollbackFor = Exception.class, value = "primaryTransactionManager")
    public void deleteByTableId(long tableId) throws IOException {

        // 如果这个表的数据已经同步到搜索引擎中
        if (indexManageMapper.existsInMappingTable(tableId) == 1) {
            String indexName = indexManageMapper.getIndexNameByTableId(tableId);
            restService.deleteDocsByTableId(indexName, tableId);
            restService.deleteAliasByIndexNameAndTableId(indexName, tableId);

        }

        dictMapper.deleteDictColumnByTableId(tableId);
        dictMapper.deleteDictTableById(tableId);

        statsMapper.deleteStatsColumnByTableId(tableId);
        statsMapper.deleteStatsTableByTableId(tableId);

        indexManageMapper.deleteMappingColumnByTableId(tableId);
        indexManageMapper.deleteMappingTableByTableId(tableId);
        indexManageMapper.deleteSeTableByTableId(tableId);
    }
}

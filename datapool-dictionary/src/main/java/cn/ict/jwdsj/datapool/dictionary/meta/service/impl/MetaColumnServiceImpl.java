package cn.ict.jwdsj.datapool.dictionary.meta.service.impl;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaColumnService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetaColumnServiceImpl implements MetaColumnService {

    @Autowired private MetaColumnRepo metaColumnRepo;
    @Autowired private DictDatabaseService dictDatabaseService;
    @Autowired private DictTableService dictTableService;
    @Autowired private DictColumnService dictColumnService;

    @Override
    public List<MetaColumn> listByDatabaseAndTable(String database, String table) {
        return metaColumnRepo.findByDatabaseAndTable(database, table);
    }

    @Override
    public List<MetaColumn> listByDatabaseAndTableIn(String database, List<String> tables) {
        return metaColumnRepo.findByDatabaseAndTableIn(database, tables);
    }

    @Override
    public List<MetaColumn> listColumnsNotAdd(long databaseId, long tableId) {
        String enDatabase = dictDatabaseService.findById(databaseId).getEnDatabase();
        String enTable = dictTableService.findById(tableId).getEnTable();

        // 该表的所有字段
        List<MetaColumn> columnsAll = this.listByDatabaseAndTable(enDatabase, enTable);

        // 已加入到字典中的字段
        Set<String> columnsAdd = dictColumnService.listColumnNameDTOsByTableId(tableId)
                .stream()
                .map(ColumnNameDTO::getEnColumn)
                .collect(Collectors.toSet());


        return columnsAll.stream().filter(column -> !columnsAdd.contains(column.getColumn())).collect(Collectors.toList());

    }
}

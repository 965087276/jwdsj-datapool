package cn.ict.jwdsj.datapool.dictionary.service.meta.impl;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.column.DictColumnMapper;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.secondary.meta.MetaColumnMapper;
import cn.ict.jwdsj.datapool.dictionary.dao.repo.database.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.dao.repo.table.DictTableRepo;
import cn.ict.jwdsj.datapool.dictionary.service.meta.MetaColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetaColumnServiceImpl implements MetaColumnService {

    @Autowired private MetaColumnMapper metaColumnMapper;
    @Autowired private DictDatabaseRepo dictDatabaseRepo;
    @Autowired private DictTableRepo dictTableRepo;
    @Autowired private DictColumnMapper dictColumnMapper;

    @Override
    public List<MetaColumn> listByDatabaseAndTable(String database, String table) {
        final String FIELD_KEY = "Field";
        final String COMMENT_KEY = "Comment";

        return metaColumnMapper.listByDatabaseAndTable(database, table)
                .stream()
                .map(map -> new MetaColumn(database, table, (String) map.get(FIELD_KEY), (String) map.get(COMMENT_KEY)))
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaColumn> listByDatabaseAndTableIn(String database, List<String> tables) {
        return tables.parallelStream()
                .flatMap(table -> this.listByDatabaseAndTable(database, table).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaColumn> listColumnsNotAdd(long databaseId, long tableId) {
        String enDatabase = dictDatabaseRepo.findById(databaseId).get().getEnDatabase();
        String enTable = dictTableRepo.findById(tableId).get().getEnTable();

        // 该表的所有字段
        List<MetaColumn> columnsAll = this.listByDatabaseAndTable(enDatabase, enTable);

        // 已加入到字典中的字段
        Set<String> columnsAdd = dictColumnMapper.listColumnNameDTOsByTableId(tableId)
                .stream()
                .map(ColumnNameDTO::getEnColumn)
                .collect(Collectors.toSet());


        return columnsAll.stream().filter(column -> !columnsAdd.contains(column.getColumn())).collect(Collectors.toList());

    }
}

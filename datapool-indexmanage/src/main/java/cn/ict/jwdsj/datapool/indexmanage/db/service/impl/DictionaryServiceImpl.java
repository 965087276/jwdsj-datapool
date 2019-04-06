package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.QMappingTable;
import cn.ict.jwdsj.datapool.indexmanage.db.service.DictionaryRemote;
import cn.ict.jwdsj.datapool.indexmanage.db.service.DictionaryService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryRemote dictionaryRemote;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DatabaseNameDTO> listDatabaseNames() {
        return dictionaryRemote.listDatabaseNames();
    }

    @Override
    public List<TableNameDTO> listTableNamesNotAddByDatabaseId(long databaseId) {
        DictDatabase dictDb = DictDatabase.buildById(databaseId);
        QDictTable dictTable = QDictTable.dictTable;
        QMappingTable mappingTable = QMappingTable.mappingTable;

        return jpaQueryFactory
                .select(dictTable.id, dictTable.enTable, dictTable.chTable)
                .from(dictTable)
                .leftJoin(mappingTable)
                .on(mappingTable.dictTable.eq(dictTable))
                .where(mappingTable.isNull().and(dictTable.dictDatabase.eq(dictDb)))
                .fetch()
                .stream()
                .map(tuple -> TableNameDTO.builder()
                        .tableId(tuple.get(dictTable.id))
                        .enTable(tuple.get(dictTable.enTable))
                        .chTable(tuple.get(dictTable.chTable))
                        .build()
                )
                .collect(Collectors.toList());


    }

    @Override
    public List<ColumnNameDTO> listColumnNamesByTableId(long tableId) {
        return dictionaryRemote.listColumnNamesByTableId(tableId);
    }


}

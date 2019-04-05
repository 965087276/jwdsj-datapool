package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.QDictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.dictionary.column.repo.DictColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@Service
public class DictColumnServiceImpl implements DictColumnService {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private DictColumnRepo dictColumnRepo;

    public List<String> getEnTableByDictDatabase(DictDatabase dictDb) {
        QDictColumn dictColumn = QDictColumn.dictColumn;
        QDictTable dictTable = QDictTable.dictTable;
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;

        return jpaQueryFactory
                .selectDistinct(dictTable.enTable)
                .from(dictTable)
                .innerJoin(dictDatabase)
                .on(dictTable.dictDatabase.eq(dictDb))
                .innerJoin(dictColumn)
                .on(dictColumn.dictTable.eq(dictTable))
                .fetch();
    }

    @Override
    @Transactional
    public void saveAll(List<DictColumn> dictColumns) {
        dictColumnRepo.saveAll(dictColumns);
    }

    @Override
    public List<ColumnNameDTO> listNamesByDictTable(DictTable dictTb) {
        QDictTable dictTable = QDictTable.dictTable;
        QDictColumn dictColumn = QDictColumn.dictColumn;

        return jpaQueryFactory
                .select(dictColumn.id, dictColumn.enColumn, dictColumn.chColumn)
                .from(dictColumn)
                .where(dictColumn.dictTable.eq(dictTb))
                .fetch()
                .stream()
                .map(tuple -> ColumnNameDTO.builder()
                        .columnId(tuple.get(dictColumn.id))
                        .enColumn(tuple.get(dictColumn.enColumn))
                        .chColumn(tuple.get(dictColumn.chColumn))
                        .build()
                )
                .collect(Collectors.toList());
    }
}

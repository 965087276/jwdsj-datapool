package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.QDictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.repo.DictColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
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
    @Autowired
    private DictTableService dictTableService;
    @Autowired
    private DictDatabaseService dictDatabaseService;

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

    @Override
    public List<DictColumnVO> listDictColumnVOs(long databaseId, long tableId) {
        DictDatabase dictDatabase = dictDatabaseService.findById(databaseId);
        DictTable dictTable = dictTableService.findById(tableId);

        return dictColumnRepo.findByDictTable(dictTable)
                .stream()
                .map(dictColumn -> this.convertToDictColumnVO(dictDatabase, dictTable, dictColumn))
                .collect(Collectors.toList());

    }

    private DictColumnVO convertToDictColumnVO(DictDatabase dictDatabase, DictTable dictTable, DictColumn dictColumn) {
        DictColumnVO dictColumnVO = BeanUtil.toBean(dictColumn, DictColumnVO.class);
        dictColumnVO.setColumnId(dictColumn.getId());
        dictColumnVO.setEnDatabase(dictDatabase.getEnDatabase());
        dictColumnVO.setChDatabase(dictDatabase.getChDatabase());
        dictColumnVO.setEnTable(dictTable.getEnTable());
        dictColumnVO.setChTable(dictTable.getChTable());
        return dictColumnVO;
    }
}

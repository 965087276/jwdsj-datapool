package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QMappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QSeTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsIndexService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class MappingTableServiceImpl implements MappingTableService {
    @Autowired
    private MappingTableRepo mappingTableRepo;
    @Autowired
    private EsColumnService esColumnService;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private ElasticRestService elasticRestService;
    @Autowired
    private EsIndexService esIndexService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MappingTableAddDTO mappingTableAddDTO) throws IOException {
        EsIndex esIndex = esIndexService.findById(mappingTableAddDTO.getIndexId());
        long dictTableId = mappingTableAddDTO.getTableId();
        long dictDatabaseId = mappingTableAddDTO.getDatabaseId();

        MappingTable mappingTable = new MappingTable();
        mappingTable.setEsIndex(esIndex);
        mappingTable.setDictTableId(dictTableId);
        mappingTable.setDictDatabaseId(dictDatabaseId);
        mappingTable.setUpdatePeriod(mappingTableAddDTO.getUpdatePeriod());
        mappingTableRepo.save(mappingTable);
        // 为该索引添加一个别名，别名为"{alias-prefix}-表id"，别名指向这个表（即filter出elastic_table_id为该表id的文档）
        elasticRestService.addAlias(esIndex.getIndexName(), dictTableId);
        // 因为这个表要加入到es中，所以要根据表字段的搜索、分词情况来给elasticsearch的索引添加字段
        esColumnService.add(mappingTableAddDTO);

        // 更新se_table表的is_sync字段为true
        QSeTable seTable = QSeTable.seTable;
        jpaQueryFactory.update(seTable).set(seTable.sync, true).where(seTable.dictTableId.eq(dictTableId)).execute();

    }

    @Override
    public List<MappingTable> listTableNeedToUpdate() {
        QMappingTable mappingTable = QMappingTable.mappingTable;
        BooleanBuilder builder = new BooleanBuilder();

        // current_date - update_date >= update_period
        builder.and(
                Expressions.dateOperation(
                        Integer.class,
                        Ops.SUB,
                        Expressions.currentDate(), mappingTable.updateDate).goe(mappingTable.updatePeriod)
        );
        return jpaQueryFactory.selectFrom(mappingTable).where(builder).fetch();
    }


}

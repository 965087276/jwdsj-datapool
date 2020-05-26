package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QMappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QSeTable;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableUpdateDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingTableVO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsIndexRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.SeTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
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
    private EsIndexRepo esIndexRepo;
    @Autowired
    private SeTableRepo seTableRepo;
    @Autowired
    private DictClient dictClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MappingTableAddDTO mappingTableAddDTO) throws IOException {

        long tableId = mappingTableAddDTO.getTableId();
        long databaseId = mappingTableAddDTO.getDatabaseId();
        long indexId = mappingTableAddDTO.getIndexId();

        EsIndex esIndex = esIndexRepo.findById(indexId);

        // 若索引中存在该表的数据，则说明之前的delete by query还没有执行完毕，因此拒绝这次增加请求
        long totalDocs = elasticRestService.getRecordsByTableIdInIndex(esIndex.getIndexName(), tableId);
        if (totalDocs != 0) {
            elasticRestService.deleteDocsByTableId(esIndex.getIndexName(), tableId);
        }
        Assert.isTrue(totalDocs == 0L, "数据正在删除中，请稍后尝试加入");

        DictTable dictTable = dictClient.findDictTableById(tableId);
        DictDatabase dictDatabase = dictClient.findDictDatabaseById(databaseId);

        MappingTable mappingTable = new MappingTable();
        mappingTable.setUpdatePeriod(RandomUtil.randomInt(20, 60));
        mappingTable.setEsIndex(esIndex);
        mappingTable.setDictTable(dictTable);
        mappingTable.setDictDatabase(dictDatabase);


        mappingTableRepo.save(mappingTable);
        // 为该索引添加一个别名，别名为"{alias-prefix}-表id"，别名指向这个表（即filter出elastic_table_id为该表id的文档）
        elasticRestService.addAlias(esIndex.getIndexName(), tableId);
        // 因为这个表要加入到es中，所以要根据表字段的搜索、分词情况来给elasticsearch的索引添加字段
        esColumnService.add(mappingTableAddDTO);

        // 更新se_table表的is_sync字段为true
        seTableRepo.updateSync(tableId, true);
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

    @Override
    public Page<MappingTableVO> listMappingTableVO(int curPage, int pageSize, long databaseId, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        QMappingTable mappingTable = QMappingTable.mappingTable;
        Predicate predicate = mappingTable.databaseId.eq(databaseId);
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, mappingTable.chTable.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, mappingTable.enTable.like('%' + nameLike + '%'));

        return mappingTableRepo.findAll(predicate, pageable).map(tb -> BeanUtil.toBean(tb, MappingTableVO.class));
    }


    /**
     * 取消数据同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableId(long tableId) throws IOException {
        String indexName = mappingTableRepo.findByTableId(tableId).getIndexName();

        mappingTableRepo.deleteByTableId(tableId);

        QSeTable seTable = QSeTable.seTable;
        // 更新seTable表的sync字段为false
        seTableRepo.updateSync(tableId, false);
        // 在elasticsearch删除该表的索引别名
        elasticRestService.deleteAliasByIndexNameAndTableId(indexName, tableId);
        // 在elasticsearch删除该表的数据
        elasticRestService.deleteDocsByTableId(indexName, tableId);

    }

    /**
     * 更新表（表的同步周期）
     *
     * @param mappingTableUpdateDTO
     */
    @Override
    public void update(MappingTableUpdateDTO mappingTableUpdateDTO) {
        MappingTable mappingTable = mappingTableRepo.findByTableId(mappingTableUpdateDTO.getTableId());
        // 发生变化时才去更新
        if (mappingTable.getUpdatePeriod() != mappingTableUpdateDTO.getUpdatePeriod()) {
            mappingTable.setUpdatePeriod(mappingTableUpdateDTO.getUpdatePeriod());
            mappingTableRepo.save(mappingTable);
        }
    }

}

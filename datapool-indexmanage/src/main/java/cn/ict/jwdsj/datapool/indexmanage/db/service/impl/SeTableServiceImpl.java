package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QSeTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.SeTable;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.SeTableVO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.SeTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class SeTableServiceImpl implements SeTableService {
    @Autowired
    private SeTableRepo seTableRepo;
    @Autowired
    private MappingColumnService mappingColumnService;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private DictClient dictClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SeTableAddDTO seTableAddDTO) {

        SeTable seTable = new SeTable();
        seTable.setDictDatabaseId(seTableAddDTO.getDatabaseId());
        seTable.setDictTableId(seTableAddDTO.getTableId());
        DictTable table = dictClient.findDictTableById(seTableAddDTO.getTableId());
        seTable.setEnTable(table.getEnTable());
        seTable.setChTable(table.getChTable());
        seTableRepo.save(seTable);
        mappingColumnService.saveAll(seTableAddDTO);
        // 更新dict_table表is_add_to_se字段为true（已加入搜索引擎模块）
        QDictTable dictTable = QDictTable.dictTable;
        jpaQueryFactory.update(dictTable).set(dictTable.addToSe, true).where(dictTable.id.eq(seTableAddDTO.getTableId())).execute();
    }

    @Override
    public Page<SeTableVO> listSeTableVO(int curPage, int pageSize, long databaseId, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        DictDatabase dictDatabase = dictClient.findDictDatabaseById(databaseId);
        QSeTable seTable = QSeTable.seTable;

        Predicate predicate = seTable.dictDatabaseId.eq(databaseId);
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, seTable.chTable.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, seTable.enTable.like('%' + nameLike + '%'));

        return seTableRepo.findAll(predicate, pageable).map(seTable1 -> this.convertToSeTableVO(dictDatabase, seTable1));
    }

    /**
     * 通过dictTableId查找
     *
     * @param dictTableId
     * @return
     */
    @Override
    public SeTable findByDictTableId(long dictTableId) {
        return seTableRepo.findByDictTableId(dictTableId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDictTableId(long dictTableId) {
        SeTable seTable = seTableRepo.findByDictTableId(dictTableId);
        Assert.isTrue(!seTable.isSync(), "该表在数据同步任务列表中，请先从同步列表中删除");
        // 删除该表设置的mappingColumn字段
        mappingColumnService.deleteByDictTableId(dictTableId);

        seTableRepo.deleteByDictTableId(dictTableId);

        // 将dictTable表中的addToSe字段设为false
        QDictTable dictTable = QDictTable.dictTable;
        jpaQueryFactory.update(dictTable).set(dictTable.addToSe, false).where(dictTable.id.eq(dictTableId)).execute();
    }

    private SeTableVO convertToSeTableVO(DictDatabase dictDatabase, SeTable seTable1) {
        SeTableVO seTableVO = BeanUtil.toBean(seTable1, SeTableVO.class);
        seTableVO.setEnDatabase(dictDatabase.getEnDatabase());
        seTableVO.setChDatabase(dictDatabase.getChDatabase());
        return seTableVO;
    }
}

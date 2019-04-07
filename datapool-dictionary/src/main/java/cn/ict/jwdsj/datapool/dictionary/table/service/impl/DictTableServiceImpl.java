package cn.ict.jwdsj.datapool.dictionary.table.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import cn.ict.jwdsj.datapool.dictionary.table.repo.DictTableRepo;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DictTableServiceImpl implements DictTableService {
    @Autowired
    private DictTableRepo dictTableRepo;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(DictTable dictTable) {
        dictTableRepo.save(dictTable);
    }

    @Override
    @Transactional
    public void saveAll(List<DictTable> dictTables) {
        dictTableRepo.saveAll(dictTables);
    }

    @Override
    public List<String> listEnTablesByDictDatabase(DictDatabase dictDatabase) {
        QDictTable dictTable = QDictTable.dictTable;

        return jpaQueryFactory
                .select(dictTable.enTable)
                .from(dictTable)
                .where(dictTable.dictDatabase.eq(dictDatabase))
                .fetch();
    }

    @Override
    public List<TbIdNameDTO> listTbIdNameDTOByDictDatabase(DictDatabase dictDatabase) {
        QDictTable dictTable = QDictTable.dictTable;
        return jpaQueryFactory
                .select(dictTable.id, dictTable.enTable)
                .from(dictTable)
                .where(dictTable.dictDatabase.eq(dictDatabase))
                .fetch()
                .stream()
                .map(tuple -> TbIdNameDTO.builder()
                        .id(tuple.get(dictTable.id))
                        .enTable(tuple.get(dictTable.enTable))
                        .build()
                )
                .collect(Collectors.toList());


    }

    @Override
    public Page<DictTableVO> listVO(int curPage, int pageSize, long databaseId, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        DictDatabase dictDatabase = DictDatabase.buildById(databaseId);
        QDictTable dictTable = QDictTable.dictTable;

        Predicate predicate = dictTable.dictDatabase.eq(dictDatabase);
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, dictTable.chTable.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, dictTable.enTable.like('%' + nameLike + '%'));

        return dictTableRepo.findAll(predicate, pageable).map(this::convertToDictTableVO);

    }

    @Override
    @Transactional
    public void saveAll(DictTableMultiAddDTO dictTableMultiAddDTO) {
        DictDatabase dictDatabase = DictDatabase.buildById(dictTableMultiAddDTO.getDatabaseId());
        List<DictTableDTO> dictTableDTOS = dictTableMultiAddDTO.getDictTables();
        // 不能有重复元素
        Assert.isTrue(dictTableDTOS.stream().distinct().count() == dictTableDTOS.size(), "有重复元素");

        List<DictTable> dictTables = dictTableDTOS
                .stream()
                .map(dictTableDTO -> this.convertToDictTable(dictTableDTO, dictDatabase))
                .collect(Collectors.toList());

        dictTableRepo.saveAll(dictTables);
    }

    private DictTableVO convertToDictTableVO(DictTable dictTable) {
        DictTableVO dictTableVO = BeanUtil.toBean(dictTable, DictTableVO.class);
        dictTableVO.setTableId(dictTable.getId());
        dictTableVO.setChDatabase(dictTable.getDictDatabase().getChDatabase());
        dictTableVO.setEnDatabase(dictTable.getDictDatabase().getEnDatabase());
        return dictTableVO;
    }
    private DictTable convertToDictTable(DictTableDTO dictTableDTO, DictDatabase dictDatabase) {
        DictTable dictTable = BeanUtil.toBean(dictTableDTO, DictTable.class);
        dictTable.setDictDatabase(dictDatabase);
        return dictTable;
    }
}

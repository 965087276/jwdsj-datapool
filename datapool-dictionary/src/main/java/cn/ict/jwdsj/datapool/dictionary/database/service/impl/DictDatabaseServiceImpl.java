package cn.ict.jwdsj.datapool.dictionary.database.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.database.repo.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictDatabaseServiceImpl implements DictDatabaseService {

    @Autowired
    private DictDatabaseRepo dictDatabaseRepo;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(DictDatabase dictDatabase) {
        dictDatabaseRepo.save(dictDatabase);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<DictDatabase> dictDatabases) {
        dictDatabaseRepo.saveAll(dictDatabases);
    }

    @Override
    public boolean exists(String enDatabase) {
        return dictDatabaseRepo.existsByEnDatabase(enDatabase);
    }

    @Override
    public Page<DictDatabaseVO> listVO(int curPage, int pageSize, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);

        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        Predicate predicate = dictDatabase.isNotNull().or(dictDatabase.isNull());
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, dictDatabase.chDatabase.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, dictDatabase.enDatabase.like('%' + nameLike + '%'));
        return dictDatabaseRepo.findAll(predicate, pageable).map(this::convertToDictDatabaseVO);
    }

    @Override
    public List<DatabaseNameDTO> listDatabaseNameDTOByIds(List<Long> ids) {
        return dictDatabaseRepo.findByIdIn(ids)
                .stream()
                .map(this::convertToDatabaseNameDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DictDatabase findById(long id) {
        return dictDatabaseRepo.findById(id).get();
    }

    private DictDatabaseVO convertToDictDatabaseVO(DictDatabase dictDatabase) {
        return BeanUtil.toBean(dictDatabase, DictDatabaseVO.class);
    }

    private DatabaseNameDTO convertToDatabaseNameDTO(DictDatabase dictDatabase) {
        DatabaseNameDTO databaseNameDTO =  BeanUtil.toBean(dictDatabase, DatabaseNameDTO.class);
        databaseNameDTO.setDatabaseId(dictDatabase.getId());
        return databaseNameDTO;
    }


}

package cn.ict.jwdsj.datapool.dictionary.database.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
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
    public void saveAll(List<DictDatabase> dictDatabases) {
        dictDatabaseRepo.saveAll(dictDatabases);
    }

    @Override
    public boolean exists(String enDatabase) {
        return dictDatabaseRepo.existsByEnDatabase(enDatabase);
    }

    @Override
    public DictDatabase findByEnDatabase(String enDatabase) {
        return dictDatabaseRepo.findByEnDatabase(enDatabase);
    }

    @Override
    public List<DatabaseNameDTO> listNames() {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        return jpaQueryFactory
                .select(dictDatabase.id, dictDatabase.enDatabase, dictDatabase.chDatabase)
                .from(dictDatabase)
                .fetch()
                .stream()
                .map(tuple -> DatabaseNameDTO.builder()
                        .databaseId(tuple.get(dictDatabase.id))
                        .enDatabase(tuple.get(dictDatabase.enDatabase))
                        .chDatabase(tuple.get(dictDatabase.chDatabase))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public Page<DictDatabaseVO> list(int curPage, int pageSize, String enNameLike, String chNameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);

        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        Predicate predicate = dictDatabase.isNotNull().or(dictDatabase.isNull());
        predicate = StrUtil.isBlank(enNameLike) ? predicate : ExpressionUtils.and(predicate, dictDatabase.enDatabase.like('%' + enNameLike + '%'));
        predicate = StrUtil.isBlank(chNameLike) ? predicate : ExpressionUtils.and(predicate, dictDatabase.chDatabase.like('%' + chNameLike + '%'));

        return dictDatabaseRepo.findAll(predicate, pageable).map(this::convertToDictDatabaseVO);
    }

//    @Override
//    public Page<DictDatabaseVO> list(int curPage, int pageSize) {
//        Pageable pageable = PageRequest.of(curPage-1, pageSize);
//        return dictDatabaseRepo.findAll(pageable).map(this::convertToDictDatabaseVO);
//    }

    private DictDatabaseVO convertToDictDatabaseVO(DictDatabase dictDatabase) {
        return BeanUtil.toBean(dictDatabase, DictDatabaseVO.class);
    }


}

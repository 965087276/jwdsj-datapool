package cn.ict.jwdsj.datapool.dictionary.meta.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.QMetaDatabase;
import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaDatabaseService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaDatabaseServiceImpl implements MetaDatabaseService {
    @Autowired
    private MetaDatabaseRepo metaDatabaseRepo;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    @Override
    public boolean exists(String database) {
        return metaDatabaseRepo.existsById(database);
    }

    @Override
    public List<String> listDatabasesNotAdd() {
        QMetaDatabase metaDatabase = QMetaDatabase.metaDatabase;
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;

        return jpaQueryFactory
                .select(metaDatabase.database)
                .from(metaDatabase)
                .leftJoin(dictDatabase)
                .on(metaDatabase.database.eq(dictDatabase.enDatabase))
                .where(dictDatabase.isNull())
                .fetch();
    }
}

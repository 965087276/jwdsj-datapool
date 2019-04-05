package cn.ict.jwdsj.datapool.dictionary.database.service.impl;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.repo.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


}

package cn.ict.jwdsj.datapool.dictionary.meta.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaColumnServiceImpl implements MetaColumnService {

    @Autowired
    private MetaColumnRepo metaColumnRepo;

    @Override
    public List<MetaColumn> listByDatabaseAndTable(String database, String table) {
        return metaColumnRepo.findByDatabaseAndTable(database, table);
    }

    @Override
    public List<MetaColumn> listByDatabaseAndTableIn(String database, List<String> tables) {
        return metaColumnRepo.findByDatabaseAndTableIn(database, tables);
    }
}

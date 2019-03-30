package cn.ict.jwdsj.datapool.dictionary.meta.service.impl;

import cn.ict.jwdsj.datapool.dictionary.meta.entity.MetaColumn;
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
}

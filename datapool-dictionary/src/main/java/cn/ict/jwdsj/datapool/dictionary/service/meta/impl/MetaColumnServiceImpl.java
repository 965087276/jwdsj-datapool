package cn.ict.jwdsj.datapool.dictionary.service.meta.impl;

import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.repository.meta.MetaColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.service.meta.MetaColumnService;
import com.netflix.discovery.converters.Auto;
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

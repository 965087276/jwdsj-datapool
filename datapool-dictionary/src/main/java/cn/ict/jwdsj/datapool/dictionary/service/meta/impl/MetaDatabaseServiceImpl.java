package cn.ict.jwdsj.datapool.dictionary.service.meta.impl;

import cn.ict.jwdsj.datapool.dictionary.dao.mapper.secondary.meta.MetaDatabaseMapper;
import cn.ict.jwdsj.datapool.dictionary.service.database.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.service.meta.MetaDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetaDatabaseServiceImpl implements MetaDatabaseService {
    @Autowired
    private MetaDatabaseMapper metaDatabaseMapper;
    @Autowired
    private DictDatabaseService dictDatabaseService;

    @Override
    public boolean exists(String database) {
        return metaDatabaseMapper.existsByDatabase(database) == 1;
    }

    @Override
    public List<String> listDatabasesNotAdd() {
        Set<String> metaDatabases = metaDatabaseMapper.listDatabase().stream().collect(Collectors.toSet());
        List<String> dictDatabases = dictDatabaseService.listEnDatabase();
        metaDatabases.removeAll(dictDatabases);
        return metaDatabases.stream().collect(Collectors.toList());
    }
}

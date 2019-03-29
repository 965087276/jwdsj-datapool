package cn.ict.jwdsj.datapool.dictionary.service.meta.impl;

import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.repository.meta.MetaTableRepo;
import cn.ict.jwdsj.datapool.dictionary.service.meta.MetaTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaTableServiceImpl implements MetaTableService {

    @Autowired
    private MetaTableRepo metaTableRepo;

    @Override
    public List<MetaTable> listByDatabase(String database) {
        return metaTableRepo.findByDatabase(database);
    }
}

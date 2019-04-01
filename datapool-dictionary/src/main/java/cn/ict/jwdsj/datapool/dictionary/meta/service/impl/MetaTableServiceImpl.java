package cn.ict.jwdsj.datapool.dictionary.meta.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaTableRepo;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaTableService;
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

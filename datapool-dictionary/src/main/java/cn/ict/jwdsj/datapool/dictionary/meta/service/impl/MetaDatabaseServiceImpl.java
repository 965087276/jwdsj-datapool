package cn.ict.jwdsj.datapool.dictionary.meta.service.impl;

import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetaDatabaseServiceImpl implements MetaDatabaseService {
    @Autowired
    private MetaDatabaseRepo metaDatabaseRepo;


    @Override
    public boolean exists(String database) {
        return metaDatabaseRepo.existsById(database);
    }
}

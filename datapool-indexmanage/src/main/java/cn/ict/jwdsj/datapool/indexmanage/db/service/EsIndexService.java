package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.indexmanage.db.entity.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;

import java.io.IOException;

public interface EsIndexService {
    void createIndex(EsIndexDTO indexDTO) throws IOException;

    EsIndex findById(long indexId);
}

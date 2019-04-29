package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;

import java.io.IOException;
import java.util.List;

public interface EsIndexService {
    void createIndex(EsIndexDTO indexDTO) throws IOException;

    EsIndex findById(long indexId);

    List<EsIndex> listAll();

    /**
     * 删除索引
     * @param indexId
     */
    void deleteIndexById(long indexId) throws IOException;
}

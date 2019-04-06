package cn.ict.jwdsj.datapool.indexmanage.elastic.service;

import cn.ict.jwdsj.datapool.indexmanage.db.entity.EsColumn;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;
import cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ElasticRestService {

    /**
     * 在Elasticsearch中创建索引
     * @param indexDTO
     */
    void createIndex(EsIndexDTO indexDTO) throws IOException;

    /**
     * 添加字段
     */
    void addFields(EsIndex esIndex, List<EsColumn> esColumns) throws IOException;
}

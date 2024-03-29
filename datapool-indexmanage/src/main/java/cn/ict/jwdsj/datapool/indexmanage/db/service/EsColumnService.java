package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;

import java.io.IOException;


public interface EsColumnService {

    /**
     * 添加表的mapping_column信息、添加elasticsearch索引的字段信息
     * @param mappingTableAddDTO 待添加的表
     */
    void add(MappingTableAddDTO mappingTableAddDTO) throws IOException;

    /**
     * 删除某索引下的字段
     * @param indexId 索引的id
     */
    void deleteByIndexId(long indexId);
}

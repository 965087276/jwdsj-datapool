package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;

import java.io.IOException;

public interface MappingTableService {
    /**
     * 前端添加表同步
     * @param mappingTableAddDTO 需同步的表的信息（库id、表id、索引id、更新周期）
     * @throws IOException
     */
    void save(MappingTableAddDTO mappingTableAddDTO) throws IOException;

}

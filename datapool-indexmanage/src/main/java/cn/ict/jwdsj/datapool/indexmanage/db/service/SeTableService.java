package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;

public interface SeTableService {
    /**
     * 搜索引擎表信息添加。
     * 前端传过来的表单，包含库id、表id，表的各字段及是否搜索、分词、索引、权重信息等
     * @param seTableAddDTO
     */
    void save(SeTableAddDTO seTableAddDTO);
}

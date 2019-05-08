package cn.ict.jwdsj.datapool.search.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;

import java.util.List;

public interface IndexManageService {
    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     * @param tableId 表id
     * @return
     */
    List<MappingColumn> listMappingColumnByTableId(long tableId);
}

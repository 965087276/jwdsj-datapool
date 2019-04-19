package cn.ict.jwdsj.datapool.search.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import java.util.List;
import java.util.concurrent.Future;

public interface IndexManageService {
    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     * @param tableId 表id
     * @return
     */
    List<ColDisplayedDTO> listColDisplayedDTOByTableId(long tableId);
}

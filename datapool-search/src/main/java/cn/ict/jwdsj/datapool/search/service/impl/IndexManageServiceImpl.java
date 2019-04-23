package cn.ict.jwdsj.datapool.search.service.impl;

import cn.ict.jwdsj.datapool.api.feign.IndexManageClient;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import cn.ict.jwdsj.datapool.search.service.IndexManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexManageServiceImpl implements IndexManageService {

    @Autowired
    private IndexManageClient indexManageClient;

    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     *
     * @param tableId 表id
     * @return
     */
    @Override
    public List<ColDisplayedDTO> listColDisplayedDTOByTableId(long tableId) {
        List<ColDisplayedDTO> list = indexManageClient.listColDisplayedDTOByTableId(tableId);
        return list;
    }
}

package cn.ict.jwdsj.datapool.search.client;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "datapool-indexmanage")
public interface IndexManageClient {

    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     * @param tableId 表id
     * @return
     */
    @GetMapping("index_manage/col_displayed_dtos")
    List<ColDisplayedDTO> listColDisplayedDTOByTableId(@RequestParam(value = "tableId") long tableId);

}

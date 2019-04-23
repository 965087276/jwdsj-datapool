package cn.ict.jwdsj.datapool.api.feign;

import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 获取全量读取数据时的字段信息
     * @return 字段信息。包括要加入到搜索引擎的字段列表，表字段到索引字段的映射。
     */
    @GetMapping("index_manage/table_full_read_dtos/tableId/{tableId}")
    TableFullReadDTO getTableFullReadDTOByTableId(@PathVariable("tableId") long tableId);

    /**
     * 获取需要更新的表
     * @return
     */
    @GetMapping("index_manage/mapping_table/need_to_update")
    List<MappingTable> getTableNeedToUpdate();
}

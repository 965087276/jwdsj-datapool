package cn.ict.jwdsj.datapool.datasync.fullread.client;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "datapool-indexmanage")
public interface IndexManageClient {
    /**
     * 获取某表需要加入到搜索引擎中的字段
     * @return 字段的名字
     */
    @GetMapping("index_manage/mapping_column_names/tableId/{tableId}")
    List<String> listColumnNamesByTableId(@PathVariable("tableId") long tableId);

    /**
     * 获取需要更新的表
     * @return
     */
    @GetMapping("index_manage/mapping_table/need_to_update")
    List<MappingTable> getTableNeedToUpdate();

}

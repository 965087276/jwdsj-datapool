package cn.ict.jwdsj.datapool.api.feign;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "datapool-datastats")
public interface StatsClient {
    @GetMapping("stats/stats_databases/ids/{ids}")
    List<StatsDatabase> listDatabasesByIds(@PathVariable("ids") String ids);

    @GetMapping("stats/stats_column/defect_column_names/tableId/{tableId}")
    List<String> getDefectColumnsByTable(@PathVariable("tableId") long tableId);

    @GetMapping("stats/stats_table/records/tableId/{tableId}")
    long getTableRecords(@PathVariable("tableId") long tableId);
}
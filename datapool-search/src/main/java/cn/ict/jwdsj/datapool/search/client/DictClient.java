package cn.ict.jwdsj.datapool.search.client;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "datapool-dictionary")
public interface DictClient {
    @GetMapping("dict/dict_databases/ids/{ids}")
    List<DictDatabase> listDatabasesByIds(@PathVariable("ids") String ids);

    @GetMapping("dict/table_name_dtos/ids/{ids}")
    List<TableNameDTO> listTableNameDTOByIdIn(@PathVariable("ids") String ids);

    @GetMapping("dict/column_name_dto/tableId/{tableId}")
    List<ColumnNameDTO> listColumnNameDTOByTableId(@PathVariable("tableId") long tableId);
}

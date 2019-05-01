package cn.ict.jwdsj.datapool.api.feign;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "datapool-dictionary")
public interface DictClient {

    /**
     * 通过表id查表信息
     * @param id
     * @return
     */
    @GetMapping("dict/dict_tables/id/{id}")
    DictTable findDictTableById(@PathVariable("id") long id);

    /**
     * 通过库id查库信息
     * @param id
     * @return
     */
    @GetMapping("dict/dict_databases/id/{id}")
    DictDatabase findDictDatabaseById(@PathVariable("id") long id);

    /**
     * 通过字段id查字段信息
     * @param id
     * @return
     */
    @GetMapping("dict/dict_columns/id/{id}")
    DictColumn findDictColumnById(@PathVariable("id") long id);

    /**
     * 通过表id查所有字段
     * @param tableId
     * @return
     */
    @GetMapping("dict/dict_columns/tableId/{tableId}")
    List<DictColumn> listDictColumnsByTableId(@PathVariable("tableId") long tableId);

    /**
     * 查多个库
     * @param ids 逗号隔开的库id
     * @return
     */
    @GetMapping("dict/dict_databases/ids/{ids}")
    List<DictDatabase> listDatabasesByIds(@PathVariable("ids") String ids);

    /**
     * 查多个table_name_dto
     * @param ids 逗号隔开的表id
     * @return
     */
    @GetMapping("dict/table_name_dtos/ids/{ids}")
    List<TableNameDTO> listTableNameDTOByIdIn(@PathVariable("ids") String ids);

    /**
     * 某表下的字段的id、英文名、中文名
     * @param tableId 表id
     * @return
     */
    @GetMapping("dict/column_name_dto/tableId/{tableId}")
    List<ColumnNameDTO> listColumnNameDTOByTableId(@PathVariable("tableId") long tableId);
}

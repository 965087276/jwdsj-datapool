package cn.ict.jwdsj.datapool.dictionary.web.remote;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DictColumnRemoteController {
    @Autowired
    private DictColumnService dictColumnService;

    @GetMapping("remote/dict/column_names")
    public List<ColumnNameDTO> listNamesByTableId(@RequestParam("tableId") long tableId) {
        DictTable dictTable = DictTable.builtById(tableId);
        return dictColumnService.listNamesByDictTable(dictTable);
    }
}

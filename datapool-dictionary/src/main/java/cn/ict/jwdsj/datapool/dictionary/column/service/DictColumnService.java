package cn.ict.jwdsj.datapool.dictionary.column.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictColumnService {
    /**
     * 获取dict_column中某库下的所有表名
     * @param dictDb
     * @return
     */
    List<String> getEnTableByDictDatabase(DictDatabase dictDb);

    @Transactional
    void saveAll(List<DictColumn> dictColumns);

    List<ColumnNameDTO> listNamesByDictTable(DictTable dictTable);
}

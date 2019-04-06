package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;

import java.util.List;

public interface DictionaryService {
    /**
     * 获取所有数据库的名字
     * @return
     */
    List<DatabaseNameDTO> listDatabaseNames();

    /**
     * 获取还未加入mapping的表的名称
     * @param databaseId
     * @return
     */
    List<TableNameDTO> listTableNamesNotAddByDatabaseId(long databaseId);

    /**
     * 获取某表的所有字段
     * @param tableId
     * @return
     */
    List<ColumnNameDTO> listColumnNamesByTableId(long tableId);
}

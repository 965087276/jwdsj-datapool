package cn.ict.jwdsj.datapool.dictionary.column.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictColumnService {
    /**
     * 获取dict_column中某库下的所有表名
     * @param dictDb
     * @return
     */
    List<String> getEnTableByDictDatabase(DictDatabase dictDb);

    void saveAll(List<DictColumn> dictColumns);

    /**
     * 获取字段id、字段名信息
     * @param dictTable 所在表
     * @return
     */
    List<ColumnNameDTO> listNamesByDictTable(DictTable dictTable);

    /**
     * 前端页字段展示列表
     * @param databaseId 所在库id
     * @param tableId 所在表id
     * @return
     */
    List<DictColumnVO> listDictColumnVOs(long databaseId, long tableId);

    void saveAll(DictColumnMultiAddDTO dictColumnMultiAddDTO);

    /**
     * 字段列表页表下拉框信息
     * @param databaseId 所在库
     * @return
     */
    List<DictTableVO> listTableDropDownBox(long databaseId);

    /**
     * 字段列表页库下拉框信息
     * @return
     */
    List<DictDatabaseVO> listDatabaseDropDownBox();
}

package cn.ict.jwdsj.datapool.dictionary.column.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.UpdateColumnDTO;

import java.util.List;

public interface DictColumnService {
    /**
     * 获取dict_column中某库下的所有表名
     * @param dictDatabaseId
     * @return
     */
    List<String> getEnTableByDictDatabaseId(long dictDatabaseId);

    void saveAll(List<DictColumn> dictColumns);

    /**
     * 前端页字段展示列表
     * @param databaseId 所在库id
     * @param tableId 所在表id
     * @return
     */
    List<DictColumnVO> listDictColumnVOs(long databaseId, long tableId);

    void saveAll(DictColumnMultiAddDTO dictColumnMultiAddDTO);


    List<TableNameDTO> listTableNameDTOByDatabaseId(long databaseId);

//    /**
//     * 字段列表页库下拉框信息
//     * @return
//     */
//    List<DatabaseNameDTO> listDatabaseDropDownBox();

    List<DictColumn> listByDictTableId(long dictTableId);

    List<ColumnNameDTO> listColumnNameDTOsByTableId(long tableId);

    /**
     * 修改字段信息
     * @param updateColumnDTO
     */
    void update(UpdateColumnDTO updateColumnDTO);

    DictColumn findById(long id);
}

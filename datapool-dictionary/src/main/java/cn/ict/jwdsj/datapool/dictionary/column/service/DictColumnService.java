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

    /**
     * 保存多个字段
     * @param dictColumns
     */
    void saveAll(List<DictColumn> dictColumns);

    /**
     * 前端页字段展示列表
     * @param databaseId 所在库id
     * @param tableId 所在表id
     * @return
     */
    List<DictColumnVO> listDictColumnVOs(long databaseId, long tableId);

    /**
     * 前端页面手动添加多个字段
     * @param dictColumnMultiAddDTO
     */
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

    /**
     * 通过id查找
     * @param id
     * @return
     */
    DictColumn findById(long id);

    /**
     * 通过id删除字段
     * @param id
     */
    void delete(long id);

    /**
     * 删除某表下的所有字段
     * @param dictTableId
     */
    void deleteByDictTableId(long dictTableId);
}

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
     * 批量插入（忽略错误）
     * @param dictColumns
     */
    void insertIgnore(List<DictColumn> dictColumns);


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


    List<DictColumn> listByTableId(long tableId);

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
     * @param tableId
     */
    void deleteByTableId(long tableId);
}

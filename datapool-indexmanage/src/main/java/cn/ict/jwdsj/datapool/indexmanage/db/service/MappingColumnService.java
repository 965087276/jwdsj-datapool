package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;

import java.util.List;

public interface MappingColumnService {

    void saveAll(SeTableAddDTO seTableAddDTO);

    List<ColumnTypeDTO> listColumnTypeDTOByTable(DictTable dictTable);

    /**
     * 表搜索引擎管理--表信息增加--加载该表的字段
     * 需要剔除空字段
     * @param databaseId 库id
     * @param tableId 表id
     * @return
     */
    List<MappingColumnVO> getInitMappingColumns(long databaseId, long tableId);

    /**
     * 获取某表需要加入到搜索引擎中的字段
     * @return 字段的名字
     */
    List<String> listColumnNamesByTableId(long tableId);
}

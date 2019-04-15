package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;

import java.util.List;
import java.util.Map;

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
     * 获取全量读取数据时的字段信息
     * @param tableId 表id
     * @return 字段信息。包括要加入到搜索引擎的字段列表，表字段到索引字段的映射。
     */
    TableFullReadDTO getTableFullReadDTOByTableId(long tableId);
}

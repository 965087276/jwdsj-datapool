package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.hutool.core.lang.Pair;
import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;

import java.util.List;
import java.util.Map;

public interface MappingColumnService {

    /**
     * 添加字段
     * @param seTableAddDTO
     */
    void saveAll(SeTableAddDTO seTableAddDTO);

    List<ColumnTypeDTO> listColumnTypeDTOByDictTableId(long dictTableId);

    /**
     * 将某表的字段按照类型和数目进行group by
     * @return
     */
    Map<String, Integer> groupWithTypeAndCountByDictTableId(long dictTableId);

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

    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     * @param tableId 表id
     * @return
     */
    List<ColDisplayedDTO> listColDisplayedDTOByTableId(long tableId);

    /**
     * 表信息管理--查看字段
     * @param tableId
     * @return
     */
    List<MappingColumnVO> listMappingColumnVOs(long tableId);

    void deleteByDictTableId(long dictTableId);

}

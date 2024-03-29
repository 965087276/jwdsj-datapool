package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
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

    /**
     * 通过columnId查找
     * @param columnId
     * @return
     */
    MappingColumn findByColumnId(long columnId);

    List<ColumnTypeDTO> listColumnTypeDTOByTableId(long tableId);

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
    List<MappingColumn> listMappingColumnByTableId(long tableId);

    /**
     * 表信息管理--查看字段
     * @param tableId
     * @return
     */
    List<MappingColumnVO> listMappingColumnVOs(long tableId);

    /**
     * 根据tableId删除数据
     * @param tableId
     */
    void deleteByTableId(long tableId);

    /**
     * 更新字段（未配置数据同步，可任意增删改字段）
     * 前台传输所有字段
     * @param seTableAddDTO
     */
    void updateColumnsNotSync(SeTableAddDTO seTableAddDTO);

    /**
     * 更新字段（已配置数据同步，可配置权重、非搜索字段的是否展示）
     * 前台只传输发生更新的字段
     * @param seTableAddDTO
     */
    void updateColumnsHasSync(SeTableAddDTO seTableAddDTO);

    /**
     * 插入/更新单条数据
     * @param mappingColumn
     */
    void save(MappingColumn mappingColumn);
}

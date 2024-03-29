package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableUpdateDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingTableVO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.io.IOException;
import java.util.List;

public interface MappingTableService {
    /**
     * 前端添加表同步
     * @param mappingTableAddDTO 需同步的表的信息（库id、表id、索引id、更新周期）
     * @throws IOException
     */
    void save(MappingTableAddDTO mappingTableAddDTO) throws IOException;


    @Deprecated
    List<MappingTable> listTableNeedToUpdate();

    Page<MappingTableVO> listMappingTableVO(int curPage, int pageSize, long databaseId, String nameLike);

    /**
     * 定时任务
     * 更新表记录数和索引记录数
     * 并将需要更新数据的表发送给datasync模块
     */
    void updateEsData();

    /**
     * 取消数据同步
     * @param tableId 被取消数据同步的表的tableId
     * @throws IOException
     */
    void deleteByTableId(long tableId) throws IOException;

    /**
     * 更新表（表的同步周期）
     * @param mappingTableUpdateDTO
     */
    void update(MappingTableUpdateDTO mappingTableUpdateDTO);


    /**
     * 手动数据同步
     */
    void syncData();
}

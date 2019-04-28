package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
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

    List<MappingTable> listTableNeedToUpdate();

    Page<MappingTableVO> listMappingTableVO(int curPage, int pageSize, long databaseId, String nameLike);

    /**
     * 定时任务，计算某表的记录数和其在搜索引擎中的记录数
     */
    void getRecordsSchedule();

    void deleteByDictTableId(long dictTableId) throws IOException;
}

package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.SeTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.SeTableVO;
import org.springframework.data.domain.Page;

public interface SeTableService {
    /**
     * 搜索引擎表信息添加。
     * 前端传过来的表单，包含库id、表id，表的各字段及是否搜索、分词、索引、权重信息等
     * @param seTableAddDTO
     */
    void save(SeTableAddDTO seTableAddDTO);

    /**
     * 表信息管理页 -- 表列表
     * @param curPage
     * @param pageSize
     * @param databaseId
     * @param nameLike 表名过滤
     * @return
     */
    Page<SeTableVO> listSeTableVO(int curPage, int pageSize, long databaseId, String nameLike);

    /**
     * 通过dictTableId查找
     * @param dictTableId
     * @return
     */
    SeTable findByDictTableId(long dictTableId);

    void deleteByDictTableId(long dictTableId);
}

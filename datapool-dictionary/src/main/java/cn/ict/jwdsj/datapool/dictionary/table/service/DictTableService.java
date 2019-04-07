package cn.ict.jwdsj.datapool.dictionary.table.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface DictTableService {

    void save(DictTable dictTable);

    @Transactional
    void saveAll(List<DictTable> dictTables);

    /**
     * 某库下的所有表
     * @param dictDatabase 库名
     * @return 英文表名的集合
     */
    List<String> listEnTablesByDictDatabase(DictDatabase dictDatabase);

    /**
     * 列出某库下所有表的id与enTable
     * @param dictDatabase
     * @return
     */
    List<TbIdNameDTO> listTbIdNameDTOByDictDatabase(DictDatabase dictDatabase);

    /**
     * 表信息管理--前端展示页
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @param databaseId 数据库id
     * @param nameLike 搜索（可查询英文表名或中文表名）
     * @return
     */
    Page<DictTableVO> listVO(int curPage, int pageSize, long databaseId, String nameLike);

    /**
     * 表信息管理--手动添加表信息
     * @param dictTableMultiAddDTO 多个表的信息
     */
    void saveAll(DictTableMultiAddDTO dictTableMultiAddDTO);

    DictTable findById(long id);
}

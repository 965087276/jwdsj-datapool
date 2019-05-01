package cn.ict.jwdsj.datapool.dictionary.table.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.UpdateTableDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface DictTableService {

    void save(DictTable dictTable);

    @Transactional(rollbackFor = Exception.class)
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

    List<TableNameDTO> listTableNameDTOByIds(List<Long> ids);

    /**
     * 表信息管理--手动添加表信息
     * @param dictTableMultiAddDTO 多个表的信息
     */
    void saveAll(DictTableMultiAddDTO dictTableMultiAddDTO);

    DictTable findById(long id);

    /**
     * 表下拉框
     * @param databaseId
     * @return
     */
    List<TableNameDTO> listTableDropDownBox(long databaseId);

    /**
     * 表信息修改
     * @param updateTableDTO
     */
    void update(UpdateTableDTO updateTableDTO);

    /**
     * 通过id删除
     * @param id
     */
    void delete(long id);

    /**
     * 判断某个库下是否有表
     * @param dictDatabase
     * @return
     */
    boolean existsByDictDatabase(DictDatabase dictDatabase);

//    /**
//     * 表信息管理--列表页--库下拉框
//     * @return 返回dict_table表中的所有数据库信息
//     */
//    List<DatabaseNameDTO> listDatabaseDropDownBox();
}

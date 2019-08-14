package cn.ict.jwdsj.datapool.dictionary.service.database;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.entity.database.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.entity.database.dto.UpdateDatabaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictDatabaseService {

    /**
     * 保存单个库信息
     * @param dictDatabase
     */
    void save(DictDatabase dictDatabase);

    /**
     * 保存多个库信息
     * @param dictDatabases
     */
    @Transactional(rollbackFor = Exception.class)
    void saveAllToDb(List<DictDatabase> dictDatabases);

    /**
     * 判断某个库是否在字典中存在
     * @param enDatabase
     * @return
     */
    boolean existsByEnDatabase(String enDatabase);

    /**
     * 字典模块--库信息管理的分页展示
     * @param curPage
     * @param pageSize
     * @param nameLike
     * @return
     */
    Page<DictDatabaseVO> listVO(int curPage, int pageSize, String nameLike);


    /**
     * 通过id查找库
     * @param id
     * @return
     */
    DictDatabase findById(long id);

    /**
     * 通过英文库名查找库
     * @param enDatabase
     * @return
     */
    DictDatabase findByEnDatabase(String enDatabase);

    /**
     * 通过多个逗号隔开的id查找库
     * @param ids
     * @return
     */
    List<DictDatabase> listByIds(String ids);

    /**
     * 库中英下拉框
     * @return
     */
    List<DatabaseNameDTO> listDatabaseDropDownBox();

    /**
     * 库信息更新
     * @param updateDatabaseDTO
     */
    void update(UpdateDatabaseDTO updateDatabaseDTO);

    /**
     * 列出所有库的英文名
     * @return
     */
    List<String> listEnDatabase();

    /**
     * 列出所有库的信息
     * @return
     */
    List<DictDatabase> listAll();

    /**
     * 删除库信息
     * @param id
     */
    void delete(long id);
}

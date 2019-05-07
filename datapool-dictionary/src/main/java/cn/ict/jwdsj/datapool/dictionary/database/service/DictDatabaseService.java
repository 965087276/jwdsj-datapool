package cn.ict.jwdsj.datapool.dictionary.database.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.database.entity.dto.UpdateDatabaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictDatabaseService {
    void save(DictDatabase dictDatabase);

    @Transactional(rollbackFor = Exception.class)
    void saveAll(List<DictDatabase> dictDatabases);

    /**
     * 判断某个库是否在字典中存在
     * @param enDatabase
     * @return
     */
    boolean exists(String enDatabase);

    /**
     * 字典模块--库信息管理的分页展示
     * @param curPage
     * @param pageSize
     * @param nameLike
     * @return
     */
    Page<DictDatabaseVO> listVO(int curPage, int pageSize, String nameLike);

//    List<DatabaseNameDTO> listDatabaseNameDTOByIds(List<Long> ids);

    DictDatabase findById(long id);
    DictDatabase findByEnDatabase(String enDatabase);

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

    List<DictDatabase> listAll();

    /**
     * 删除库信息
     * @param id
     */
    void delete(long id);
}

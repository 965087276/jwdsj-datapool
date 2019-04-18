package cn.ict.jwdsj.datapool.dictionary.database.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictDatabaseService {
    void save(DictDatabase dictDatabase);

    @Transactional(rollbackFor = Exception.class)
    void saveAll(List<DictDatabase> dictDatabases);

    boolean exists(String enDatabase);

    Page<DictDatabaseVO> listVO(int curPage, int pageSize, String nameLike);

//    List<DatabaseNameDTO> listDatabaseNameDTOByIds(List<Long> ids);

    DictDatabase findById(long id);

    List<DictDatabase> listByIds(String ids);

    /**
     * 库中英下拉框
     * @return
     */
    List<DatabaseNameDTO> listDatabaseDropDownBox();
}

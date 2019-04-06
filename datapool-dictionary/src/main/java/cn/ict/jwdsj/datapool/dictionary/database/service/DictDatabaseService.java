package cn.ict.jwdsj.datapool.dictionary.database.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictDatabaseService {
    void save(DictDatabase dictDatabase);

    @Transactional
    void saveAll(List<DictDatabase> dictDatabases);

    boolean exists(String enDatabase);

    DictDatabase findByEnDatabase(String enDatabase);

    List<DatabaseNameDTO> listNames();

    Page<DictDatabaseVO> list(int curPage, int pageSize, String enNameLike, String chNameLike);
}

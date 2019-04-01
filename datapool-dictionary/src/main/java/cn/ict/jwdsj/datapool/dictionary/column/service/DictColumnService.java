package cn.ict.jwdsj.datapool.dictionary.column.service;

import cn.ict.jwdsj.datapool.dictionary.column.entity.DictColumn;
import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictColumnService {
    /**
     * 获取dict_column中某库下的所有表名
     * @param dictDb
     * @return
     */
    List<String> getEnTableByDictDatabase(DictDatabase dictDb);

    @Transactional
    void saveAll(List<DictColumn> dictColumns);
}

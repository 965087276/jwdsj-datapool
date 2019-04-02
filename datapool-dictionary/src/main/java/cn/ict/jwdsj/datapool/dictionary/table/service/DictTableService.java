package cn.ict.jwdsj.datapool.dictionary.table.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
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

    List<TbIdNameDTO> listTbIdNameDTOByDictDatabase(DictDatabase dictDatabase);
}

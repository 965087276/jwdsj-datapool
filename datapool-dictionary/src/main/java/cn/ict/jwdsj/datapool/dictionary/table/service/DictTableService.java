package cn.ict.jwdsj.datapool.dictionary.table.service;

import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.table.entity.DictTable;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictTableService {

    void save(DictTable dictTable);

    @Transactional
    void saveAll(List<DictTable> dictTables);

    List<DictTable> listByDictDatabase(DictDatabase dictDatabase);


    List<TbIdNameDTO> listTbIdNameDTOByDictDatabase(DictDatabase dictDatabase);
}

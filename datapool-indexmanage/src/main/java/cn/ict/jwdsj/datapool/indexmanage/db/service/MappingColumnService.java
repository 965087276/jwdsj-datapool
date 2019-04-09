package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingColumnDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;

import java.util.List;

public interface MappingColumnService {

    void saveAll(SeTableAddDTO seTableAddDTO);

    List<ColumnTypeDTO> listColumnTypeDTOByTable(DictTable dictTable);

}

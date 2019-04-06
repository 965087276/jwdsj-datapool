package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.TableAddDTO;

import java.util.List;

public interface MappingColumnService {
    void save(TableAddDTO tableAddDTO);

    List<ColumnTypeDTO> listColumnTypeDTOByTable(DictTable dictTable);
}

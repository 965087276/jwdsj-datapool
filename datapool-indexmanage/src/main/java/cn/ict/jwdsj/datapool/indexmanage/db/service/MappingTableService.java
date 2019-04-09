package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;

import java.io.IOException;

public interface MappingTableService {
    void save(MappingTableAddDTO mappingTableAddDTO) throws IOException;

//    void save(SeTableAddDTO tableAddDTO);

}

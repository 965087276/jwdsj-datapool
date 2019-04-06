package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.MappingTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class MappingTableServiceImpl implements MappingTableService {
    @Autowired
    private MappingTableRepo mappingTableRepo;
    @Autowired
    private EsColumnService esColumnService;
    @Autowired
    private MappingColumnService mappingColumnService;

    @Override
    @Transactional
    public void save(MappingTableAddDTO mappingTableAddDTO) throws IOException {
        EsIndex esIndex = EsIndex.builtById(mappingTableAddDTO.getIndexId());
        DictTable dictTable = DictTable.builtById(mappingTableAddDTO.getTableId());

        MappingTable mappingTable = new MappingTable();
        mappingTable.setEsIndex(esIndex);
        mappingTable.setDictTable(dictTable);
        mappingTableRepo.save(mappingTable);

        esColumnService.add(mappingTableAddDTO);
    }



}

package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
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

    @Override
    @Transactional
    public void save(MappingTableAddDTO mappingTableAddDTO) throws IOException {
        EsIndex esIndex = EsIndex.builtById(mappingTableAddDTO.getIndexId());
        DictTable dictTable = DictTable.builtById(mappingTableAddDTO.getTableId());
        DictDatabase dictDatabase = DictDatabase.buildById(mappingTableAddDTO.getDatabaseId());

        MappingTable mappingTable = new MappingTable();
        mappingTable.setEsIndex(esIndex);
        mappingTable.setDictTable(dictTable);
        mappingTable.setDictDatabase(dictDatabase);
        mappingTable.setUpdatePeriod(mappingTableAddDTO.getUpdatePeriod());
        mappingTableRepo.save(mappingTable);
        // 因为这个表要加入到es中，所以要根据表字段的搜索、分词情况来给elasticsearch的索引添加字段
        esColumnService.add(mappingTableAddDTO);
    }



}

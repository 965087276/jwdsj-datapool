package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.SeTable;
import cn.ict.jwdsj.datapool.indexmanage.client.DictClient;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.SeTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeTableServiceImpl implements SeTableService {
    @Autowired
    private SeTableRepo seTableRepo;
    @Autowired
    private MappingColumnService mappingColumnService;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private DictClient dictClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SeTableAddDTO seTableAddDTO) {

        SeTable seTable = new SeTable();
        seTable.setDictDatabaseId(seTableAddDTO.getDatabaseId());
        seTable.setDictTableId(seTableAddDTO.getTableId());
        DictTable table = dictClient.findDictTableById(seTableAddDTO.getTableId());
        table.setEnTable(table.getEnTable());
        table.setChTable(table.getChTable());
        seTableRepo.save(seTable);
        mappingColumnService.saveAll(seTableAddDTO);
        // 更新dict_table表is_add_to_se字段为true（已加入搜索引擎模块）
        QDictTable dictTable = QDictTable.dictTable;
        jpaQueryFactory.update(dictTable).set(dictTable.addToSe, true).where(dictTable.id.eq(seTableAddDTO.getTableId())).execute();
    }
}

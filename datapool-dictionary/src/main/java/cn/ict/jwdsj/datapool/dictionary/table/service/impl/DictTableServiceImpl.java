package cn.ict.jwdsj.datapool.dictionary.table.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import cn.ict.jwdsj.datapool.dictionary.table.repo.DictTableRepo;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictTableServiceImpl implements DictTableService {
    @Autowired
    private DictTableRepo dictTableRepo;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(DictTable dictTable) {
        dictTableRepo.save(dictTable);
    }

    @Override
    public void saveAll(List<DictTable> dictTables) {
        dictTableRepo.saveAll(dictTables);
    }

    @Override
    public List<DictTable> listByDictDatabase(DictDatabase dictDatabase) {
        return dictTableRepo.findByDictDatabase(dictDatabase);
    }

    @Override
    public List<TbIdNameDTO> listTbIdNameDTOByDictDatabase(DictDatabase dictDatabase) {
        QDictTable dictTable = QDictTable.dictTable;
        return jpaQueryFactory
                .select(dictTable.id, dictTable.enTable)
                .from(dictTable)
                .where(dictTable.dictDatabase.eq(dictDatabase))
                .fetch()
                .stream()
                .map(tuple -> TbIdNameDTO.builder()
                        .id(tuple.get(dictTable.id))
                        .enTable(tuple.get(dictTable.enTable))
                        .build()
                )
                .collect(Collectors.toList());


    }
}

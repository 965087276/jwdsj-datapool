package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.ict.jwdsj.datapool.dictionary.column.entity.DictColumn;
import cn.ict.jwdsj.datapool.dictionary.column.entity.QDictColumn;
import cn.ict.jwdsj.datapool.dictionary.column.repo.DictColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.entity.QDictDatabase;
import cn.ict.jwdsj.datapool.dictionary.table.entity.QDictTable;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@Service
public class DictColumnServiceImpl implements DictColumnService {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private DictColumnRepo dictColumnRepo;

    public List<String> getEnTableByDictDatabase(DictDatabase dictDb) {
        QDictColumn dictColumn = QDictColumn.dictColumn;
        QDictTable dictTable = QDictTable.dictTable;
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;

        return jpaQueryFactory
                .selectDistinct(dictTable.enTable)
                .from(dictTable)
                .innerJoin(dictDatabase)
                .on(dictTable.dictDatabase.eq(dictDb))
                .innerJoin(dictColumn)
                .on(dictColumn.dictTable.eq(dictTable))
                .fetch();
    }

    @Override
    @Transactional
    public void saveAll(List<DictColumn> dictColumns) {
        int N = 4000; // 将大list分割多个小list，4000个一组
        int numSubList = dictColumns.size() / N + (dictColumns.size() % N == 0 ? 0 : 1);
        IntStream.range(0, numSubList)
                .mapToObj(i ->
                        dictColumns.subList(i * N, Math.min(i * N + N, dictColumns.size()))
                )
                .collect(Collectors.toList())
                .forEach(dictColumnRepo::saveAll);
    }
}

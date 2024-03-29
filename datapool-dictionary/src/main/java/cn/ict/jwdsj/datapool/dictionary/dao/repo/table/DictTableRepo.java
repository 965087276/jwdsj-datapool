package cn.ict.jwdsj.datapool.dictionary.dao.repo.table;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictTableRepo extends JpaRepository<DictTable, Long>, QuerydslPredicateExecutor<DictTable> {

    List<DictTable> findByDictDatabase(DictDatabase dictDatabase);

    List<DictTable> findByIdIn(List<Long> ids);

    boolean existsByDictDatabase(DictDatabase dictDatabase);

    DictTable findByEnDatabaseAndEnTable(String enDatabase, String enTable);

}

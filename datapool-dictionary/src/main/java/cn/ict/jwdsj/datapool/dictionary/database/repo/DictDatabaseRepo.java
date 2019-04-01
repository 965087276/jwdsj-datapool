package cn.ict.jwdsj.datapool.dictionary.database.repo;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDatabaseRepo extends JpaRepository<DictDatabase, Long>, QuerydslPredicateExecutor<DictDatabase> {
    boolean existsByEnDatabase(String enDatabase);
    DictDatabase findByEnDatabase(String enDatabase);
}

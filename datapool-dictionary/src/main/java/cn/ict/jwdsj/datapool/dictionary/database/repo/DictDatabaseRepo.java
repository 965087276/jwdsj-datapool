package cn.ict.jwdsj.datapool.dictionary.database.repo;

import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDatabaseRepo extends JpaRepository<DictDatabase, Long> {
    boolean existsByEnDatabase(String enDatabase);
    DictDatabase findByEnDatabase(String enDatabase);
}

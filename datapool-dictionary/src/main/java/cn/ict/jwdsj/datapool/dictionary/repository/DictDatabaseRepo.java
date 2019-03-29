package cn.ict.jwdsj.datapool.dictionary.repository;

import cn.ict.jwdsj.datapool.dictionary.entity.DictDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDatabaseRepo extends JpaRepository<DictDatabase, Long> {
}

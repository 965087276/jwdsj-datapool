package cn.ict.jwdsj.datapool.dictionary.repository;

import cn.ict.jwdsj.datapool.dictionary.entity.DictDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictDatabaseRepo extends JpaRepository<DictDatabase, Long> {
}

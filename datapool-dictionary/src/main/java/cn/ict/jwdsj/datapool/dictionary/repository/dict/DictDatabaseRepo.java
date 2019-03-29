package cn.ict.jwdsj.datapool.dictionary.repository.dict;

import cn.ict.jwdsj.datapool.dictionary.entity.dict.DictDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDatabaseRepo extends JpaRepository<DictDatabase, Long> {
}

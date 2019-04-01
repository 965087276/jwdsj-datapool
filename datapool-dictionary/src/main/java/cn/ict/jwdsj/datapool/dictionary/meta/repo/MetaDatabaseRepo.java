package cn.ict.jwdsj.datapool.dictionary.meta.repo;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaDatabaseRepo extends JpaRepository<MetaDatabase, String> {

}

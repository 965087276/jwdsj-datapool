package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.indexmanage.db.entity.EsIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsIndexRepo extends JpaRepository<EsIndex, Long> {
    EsIndex findById(long id);
}

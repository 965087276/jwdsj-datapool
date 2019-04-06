package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.indexmanage.db.entity.EsColumn;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.EsIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsColumnRepo extends JpaRepository<EsColumn, Long> {
    int countByEsIndexAndType(EsIndex esIndex, String type);
    boolean existsByEsIndexAndName(EsIndex esIndex, String name);

}

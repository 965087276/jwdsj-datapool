package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsColumnRepo extends JpaRepository<EsColumn, Long> {
    int countByEsIndexAndType(EsIndex esIndex, String type);
    boolean existsByEsIndexAndName(EsIndex esIndex, String name);

}

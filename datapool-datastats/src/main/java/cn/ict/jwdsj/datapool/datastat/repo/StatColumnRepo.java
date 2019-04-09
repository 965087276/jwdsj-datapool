package cn.ict.jwdsj.datapool.datastat.repo;

import cn.ict.jwdsj.datapool.datastat.entity.StatColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatColumnRepo extends JpaRepository<StatColumn, Long> {
}

package cn.ict.jwdsj.datapool.datastat.repo;


import cn.ict.jwdsj.datapool.common.entity.datastats.StatTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatTableRepo extends JpaRepository<StatTable, Long> {

}

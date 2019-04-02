package cn.ict.jwdsj.datapool.datastat.repo;


import cn.ict.jwdsj.datapool.datastat.entity.StatTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatTableRepo extends JpaRepository<StatTable, Long> {

}

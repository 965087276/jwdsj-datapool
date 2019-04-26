package cn.ict.jwdsj.datapool.datastat.repo;


import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatsTableRepo extends JpaRepository<StatsTable, Long> {
    StatsTable findByDictTableId(long dictTableId);
}

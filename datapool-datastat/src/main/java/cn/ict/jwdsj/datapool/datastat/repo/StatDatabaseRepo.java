package cn.ict.jwdsj.datapool.datastat.repo;

import cn.ict.jwdsj.datapool.datastat.entity.StatDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatDatabaseRepo extends JpaRepository<StatDatabase, Long> {
    void deleteAllByIdIn(List<Long> ids);
}

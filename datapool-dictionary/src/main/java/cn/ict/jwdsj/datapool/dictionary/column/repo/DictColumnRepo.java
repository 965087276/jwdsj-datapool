package cn.ict.jwdsj.datapool.dictionary.column.repo;

import cn.ict.jwdsj.datapool.dictionary.column.entity.DictColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictColumnRepo extends JpaRepository<DictColumn, Long> {
}

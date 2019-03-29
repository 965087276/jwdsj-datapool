package cn.ict.jwdsj.datapool.dictionary.repository;

import cn.ict.jwdsj.datapool.dictionary.entity.DictColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictColumnRepo extends JpaRepository<DictColumn, Long> {
}

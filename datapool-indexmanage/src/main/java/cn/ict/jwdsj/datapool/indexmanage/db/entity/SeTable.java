package cn.ict.jwdsj.datapool.indexmanage.db.entity;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 搜索引擎表，包含了已加入到同步队列和未加入到同步队列中的表。
 * 即配置了column_mapping的表的集合。
 */
@Entity
@Table(name = "se_table")
@Data
public class SeTable extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "database_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private DictDatabase dictDatabase;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private DictTable dictTable;

    @Column(name = "is_sync")
    private boolean sync = false;
}

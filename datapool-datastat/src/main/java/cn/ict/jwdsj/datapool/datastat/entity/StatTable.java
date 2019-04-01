package cn.ict.jwdsj.datapool.datastat.entity;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stat_table")
@Data
public class StatTable extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private DictTable dictTable;

    @Column(name = "total_records", nullable = false)
    private long totalRecords = 0L;

    @Column(name = "update_date", nullable = false)
    private Date updateDate = new Date();

    public static StatTable builtByTableId(long id) {
        StatTable statTable = new StatTable();
        statTable.setDictTable(DictTable.builtById(id));
        return statTable;
    }



}

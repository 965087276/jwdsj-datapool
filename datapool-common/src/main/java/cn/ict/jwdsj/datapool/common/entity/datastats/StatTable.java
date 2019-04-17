package cn.ict.jwdsj.datapool.common.entity.datastats;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
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
    @JoinColumn(name = "database_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private DictDatabase dictDatabase;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private DictTable dictTable;

    @Column(name = "total_records", nullable = false)
    private long totalRecords = 0L;

    @Column(name = "update_date", nullable = false)
    private Date updateDate;

    public StatTable() {}

    public StatTable id(long id) {
        this.setId(id);
        return this;
    }

    public StatTable dictDatabase(long id) {
        this.setDictDatabase(DictDatabase.buildById(id));
        return this;
    }

    public StatTable dictTable(long id) {
        this.setDictTable(DictTable.builtById(id));
        return this;
    }

    public StatTable updateDate(Date date) {
        this.setUpdateDate(date);
        return this;
    }

    public static StatTable builtById(long id) {
        StatTable statTable = new StatTable();
        statTable.setId(id);
        return statTable;
    }

}

package cn.ict.jwdsj.datapool.common.entity.datastats;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "stat_table")
@Data
public class StatsTable extends BaseEntity {

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
    private LocalDate updateDate;

    public StatsTable() {}

    public StatsTable id(long id) {
        this.setId(id);
        return this;
    }

    public StatsTable dictDatabase(long id) {
        this.setDictDatabase(DictDatabase.buildById(id));
        return this;
    }

    public StatsTable dictTable(long id) {
        this.setDictTable(DictTable.builtById(id));
        return this;
    }

    public StatsTable updateDate(LocalDate date) {
        this.setUpdateDate(date);
        return this;
    }

    public static StatsTable builtById(long id) {
        StatsTable statsTable = new StatsTable();
        statsTable.setId(id);
        return statsTable;
    }

}

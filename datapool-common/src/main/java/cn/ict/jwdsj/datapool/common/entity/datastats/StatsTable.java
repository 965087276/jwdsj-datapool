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

//    @ManyToOne
//    @JoinColumn(name = "database_id", nullable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictDatabase dictDatabase;
//
//    @ManyToOne
//    @JoinColumn(name = "table_id", nullable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictTable dictTable;

    @Column(name = "database_id")
    private long dictDatabaseId;

    @Column(name = "table_id")
    private long dictTableId;

    @Column(name = "total_records", nullable = false)
    private long totalRecords = 0L;

    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;

    @Column(name = "en_table")
    private String enTable;

    @Column(name = "ch_table")
    private String chTable;

    private int totalColumns = 0;

    private int defectColumns = 0;

    private float defectRate = 0;

    public StatsTable() {}

    public StatsTable enTable(String enTable) {
        this.setEnTable(enTable);
        return this;
    }

    public StatsTable chTable(String chTable) {
        this.setChTable(chTable);
        return this;
    }

    public StatsTable id(long id) {
        this.setId(id);
        return this;
    }

    public StatsTable dictDatabaseId(long id) {
        this.setDictDatabaseId(id);
        return this;
    }

    public StatsTable dictTableId(long id) {
        this.setDictTableId(id);
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

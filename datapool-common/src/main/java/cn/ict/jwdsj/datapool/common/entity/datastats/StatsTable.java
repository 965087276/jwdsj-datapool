package cn.ict.jwdsj.datapool.common.entity.datastats;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stat_table")
@Data
@EqualsAndHashCode(exclude = {"defectRate"})
public class StatsTable extends BaseEntity implements Cloneable {

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
    private long databaseId;

    @Column(name = "table_id")
    private long tableId;

    @Column(name = "total_records", nullable = false)
    private long totalRecords = 0L;

    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;

    @Column(name = "en_table")
    private String enTable;

    @Column(name = "ch_table")
    private String chTable;

    @Column(name = "total_columns")
    private int totalColumns = 0;

    @Column(name = "defect_columns")
    private int defectColumns = 0;

    @Column(name = "defect_rate")
    private float defectRate = 0;

    @Override
    public Object clone() {
        StatsTable obj = null;
        try{
            obj = (StatsTable) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void setUpdateDate(LocalDate date) {
        if (date == null) {
            this.updateDate = LocalDate.of(2009,1,1);
        } else {
            this.updateDate = date;
        }
    }

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

    public StatsTable databaseId(long id) {
        this.setDatabaseId(id);
        return this;
    }

    public StatsTable tableId(long id) {
        this.setTableId(id);
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

    public void setDefectRate() {
        if (this.totalColumns == 0) {
            this.defectRate = 0;
        }
        else {
            this.defectRate = (float) this.defectColumns / this.totalColumns;
        }
    }
}

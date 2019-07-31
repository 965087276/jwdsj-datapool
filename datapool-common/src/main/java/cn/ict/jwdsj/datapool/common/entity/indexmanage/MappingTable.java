package cn.ict.jwdsj.datapool.common.entity.indexmanage;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mapping_table")
@Data
public class MappingTable extends BaseEntity {

//    @ManyToOne
//    @JoinColumn(name = "database_id", nullable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictDatabase dictDatabase;

    @Column(name = "database_id", nullable = false)
    private long databaseId;

//    @ManyToOne
//    @JoinColumn(name = "table_id", nullable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictTable dictTable;

    @Column(name = "table_id", nullable = false)
    private long tableId;

    @Column(name = "index_id", nullable = false)
    private long indexId;

    @Column(name = "en_database")
    private String enDatabase;

    @Column(name = "en_table", nullable = false)
    private String enTable;

    @Column(name = "ch_table", nullable = false)
    private String chTable;

    @Column(name = "index_name", nullable = false)
    private String indexName;

//    @ManyToOne
//    @JoinColumn(name = "index_id", nullable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    private EsIndex esIndex;

    @Column(name = "table_records", nullable = false)
    private long tableRecords = 0L;

    @Column(name = "index_records", nullable = false)
    private long indexRecords = 0L;

    @Column(name = "update_period", nullable = false)
    private int updatePeriod;

    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate = LocalDate.of(2009, 1, 1);

    public void setDictTable(DictTable dictTable) {
        this.setTableId(dictTable.getId());
        this.setEnTable(dictTable.getEnTable());
        this.setChTable(dictTable.getChTable());
    }

    public void setEsIndex(EsIndex esIndex) {
        this.setIndexId(esIndex.getId());
        this.setIndexName(esIndex.getIndexName());
    }

    public void setDictDatabase(DictDatabase dictDatabase) {
        this.setDatabaseId(dictDatabase.getId());
        this.setEnDatabase(dictDatabase.getEnDatabase());
    }
}

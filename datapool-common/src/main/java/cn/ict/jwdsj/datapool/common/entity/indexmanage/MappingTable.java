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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "database_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private DictDatabase dictDatabase;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private DictTable dictTable;

    @ManyToOne
    @JoinColumn(name = "index_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private EsIndex esIndex;

    @Column(name = "table_records", nullable = false)
    private long tableRecords = 0L;

    @Column(name = "index_records", nullable = false)
    private long indexRecords = 0L;

    @Column(name = "update_period", nullable = false)
    private int updatePeriod;

    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate = LocalDate.of(2009, 1, 1);

}

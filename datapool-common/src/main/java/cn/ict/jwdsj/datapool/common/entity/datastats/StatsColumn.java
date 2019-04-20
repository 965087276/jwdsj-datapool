package cn.ict.jwdsj.datapool.common.entity.datastats;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stat_column")
@Data
public class StatsColumn extends BaseEntity {

    @Column(name = "database_id", nullable = false)
    private long dictDatabaseId;

    @Column(name = "table_id", nullable = false)
    private long dictTableId;

    @Column(name = "column_id", nullable = false)
    private long dictColumnId;

    @Column(name = "en_column", nullable = false)
    private String enColumn;

    @Column(name = "ch_column", nullable = false)
    private String chColumn;

    @Column(name = "date", nullable = false)
    private Date updateDate;

    @Column(name = "is_defect", nullable = false)
    private boolean defected;
}

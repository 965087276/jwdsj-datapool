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

    @Column(name = "database_id")
    private long dictDatabaseId;

    @Column(name = "table_id")
    private long dictTableId;

    @Column(name = "column_id")
    private long dictColumnId;

    @Column(name = "date")
    private Date updateDate;

    @Column(name = "is_defect")
    private boolean defected;
}

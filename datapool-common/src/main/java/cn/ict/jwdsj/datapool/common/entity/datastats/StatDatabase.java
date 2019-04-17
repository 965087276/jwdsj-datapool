package cn.ict.jwdsj.datapool.common.entity.datastats;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stat_database")
@Data
public class StatDatabase extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "database_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private DictDatabase dictDatabase;

    @Column(name = "total_tables", nullable = false)
    private int totalTables = 0;

    @Column(name = "total_records", nullable = false)
    private long totalRecords = 0L;

    @Column(name = "update_date", nullable = false)
    private Date updateDate = new Date();

    public static StatDatabase builtByDatabaseId(long id) {
        StatDatabase statDatabase = new StatDatabase();
        statDatabase.setDictDatabase(DictDatabase.buildById(id));
        return statDatabase;
    }
}

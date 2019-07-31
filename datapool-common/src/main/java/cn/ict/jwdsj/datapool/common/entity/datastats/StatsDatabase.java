package cn.ict.jwdsj.datapool.common.entity.datastats;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "stat_database")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsDatabase extends BaseEntity {
//    @ManyToOne
//    @JoinColumn(name = "database_id")
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictDatabase dictDatabase;

    @Column(name = "database_id")
    private long databaseId;

    @Column(name = "en_database")
    private String enDatabase;

    @Column(name = "ch_database")
    private String chDatabase;

    @Builder.Default
    @Column(name = "total_tables", nullable = false)
    private int totalTables = 0;

    @Builder.Default
    @Column(name = "total_records", nullable = false)
    private long totalRecords = 0L;

    @Builder.Default
    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate = LocalDate.of(2018, 1, 1);

}

package cn.ict.jwdsj.datapool.search.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class AggDatabaseVO implements Serializable {
    private long databaseId;
    private String enDatabase;
    private String chDatabase;
    private String detail;
    private long totalHit;
    private LocalDate updateDate;
}

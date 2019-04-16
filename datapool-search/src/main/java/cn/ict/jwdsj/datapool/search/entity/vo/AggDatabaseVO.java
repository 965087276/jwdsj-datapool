package cn.ict.jwdsj.datapool.search.entity.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AggDatabaseVO {
    private long databaseId;
    private String chDatabase;
    private String detail;
    private long totalHit;
    private LocalDate updateDate;
}

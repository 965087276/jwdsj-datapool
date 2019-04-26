package cn.ict.jwdsj.datapool.indexmanage.db.entity.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MappingTableVO {
    private long id;
    private String enTable;
    private String chTable;
    private String indexName;
    private long tableRecords;
    private long indexRecords;
    private int updatePeriod;
    private LocalDate updateDate;
}

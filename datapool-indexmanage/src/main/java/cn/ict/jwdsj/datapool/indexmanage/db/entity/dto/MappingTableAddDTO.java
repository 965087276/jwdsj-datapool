package cn.ict.jwdsj.datapool.indexmanage.db.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class MappingTableAddDTO {
    @Min(value = 1)
    private long databaseId;
    @Min(value = 1)
    private long tableId;
    @Min(value = 1)
    private long indexId;
    @Min(value = 1)
    private int updatePeriod;
}

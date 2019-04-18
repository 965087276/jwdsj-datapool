package cn.ict.jwdsj.datapool.search.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AggTableVO {
    private long tableId;
    private String enTable;
    private String chTable;
    private long totalHit;
}

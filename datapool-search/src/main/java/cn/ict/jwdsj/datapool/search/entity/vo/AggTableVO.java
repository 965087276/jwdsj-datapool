package cn.ict.jwdsj.datapool.search.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AggTableVO implements Serializable {
    private long tableId;
    private String enTable;
    private String chTable;
    private long totalHit;
}

package cn.ict.jwdsj.datapool.common.dto.dictionary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableNameDTO {
    private long tableId;
    private String enTable;
    private String chTable;
}

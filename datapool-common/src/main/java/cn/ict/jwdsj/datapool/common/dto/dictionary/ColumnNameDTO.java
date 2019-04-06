package cn.ict.jwdsj.datapool.common.dto.dictionary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnNameDTO {
    private long columnId;
    private String enColumn;
    private String chColumn;
}

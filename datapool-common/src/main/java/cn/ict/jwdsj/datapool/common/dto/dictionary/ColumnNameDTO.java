package cn.ict.jwdsj.datapool.common.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnNameDTO {
    private long columnId;
    private String enColumn;
    private String chColumn;
}

package cn.ict.jwdsj.datapool.common.dto.dictionary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseNameDTO {
    private long databaseId;
    private String enDatabase;
    private String chDatabase;
}

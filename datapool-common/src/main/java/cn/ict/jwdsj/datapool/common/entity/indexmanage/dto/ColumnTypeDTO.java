package cn.ict.jwdsj.datapool.common.entity.indexmanage.dto;

import lombok.Builder;
import lombok.Data;

/**
 *  MappingColumn的封装，一个字段的esName及其type
 */
@Data
@Builder
public class ColumnTypeDTO {
    private long columnId;
    private String esColumn;
    private String type;
}

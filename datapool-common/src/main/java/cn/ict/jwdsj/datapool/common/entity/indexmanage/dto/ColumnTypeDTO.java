package cn.ict.jwdsj.datapool.common.entity.indexmanage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  MappingColumn的封装，一个字段的esName及其type
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnTypeDTO {
    private long columnId;
    private String name;
    private String type;
}

package cn.ict.jwdsj.datapool.indexmanage.db.entity.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 表信息管理模块--添加表信息（包括库名、表名、字段信息）
 */
@Data
public class SeTableAddDTO {
    @Min(value = 1)
    private long databaseId;

    @Min(value = 1)
    private long tableId;

    @Valid
    @NotEmpty(message = "字段为空，请先加入字段的中英对照")
    List<MappingColumnDTO> columns;
}

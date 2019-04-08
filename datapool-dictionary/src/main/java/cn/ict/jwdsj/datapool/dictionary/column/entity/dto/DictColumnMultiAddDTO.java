package cn.ict.jwdsj.datapool.dictionary.column.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class DictColumnMultiAddDTO {

    @Min(value = 1)
    private long databaseId;

    @Min(value = 1)
    private long tableId;

    @NotEmpty(message = "字段信息为空，请先添加字段信息")
    List<DictColumnAddDTO> dictColumnAddDTOS;

}

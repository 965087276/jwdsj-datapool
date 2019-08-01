package cn.ict.jwdsj.datapool.dictionary.column.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class DictColumnMultiAddDTO {

    @Min(value = 1)
    private long databaseId;

    @Min(value = 1)
    private long tableId;

    @NotBlank(message = "英文库名为空")
    private String enDatabase;

    @NotBlank(message = "英文表名为空")
    private String enTable;

    @NotEmpty(message = "字段信息为空，请先添加字段信息")
    List<DictColumnAddDTO> dictColumnAddDTOS;

}

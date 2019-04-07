package cn.ict.jwdsj.datapool.dictionary.table.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class DictTableMultiAddDTO {
    @Min(value = 1)
    private long databaseId;
    @NotEmpty
    private List<DictTableDTO> dictTables;
}

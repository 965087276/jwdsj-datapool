package cn.ict.jwdsj.datapool.dictionary.table.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"chTable"})
public class DictTbExcelDTO {
    private String enTable;
    private String chTable;
}

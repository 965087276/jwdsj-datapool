package cn.ict.jwdsj.datapool.dictionary.entity.column.vo;

import lombok.Data;

@Data
public class DictColumnVO {
    private String enDatabase;
//    private String chDatabase;
    private String enTable;
//    private String chTable;
    private long columnId;
    private String enColumn;
    private String chColumn;
}

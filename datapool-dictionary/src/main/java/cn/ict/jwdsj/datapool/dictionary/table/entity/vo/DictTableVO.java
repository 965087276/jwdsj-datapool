package cn.ict.jwdsj.datapool.dictionary.table.entity.vo;

import lombok.Data;

@Data
public class DictTableVO {
    private String enDatabase;
    private String chDatabase;
    private long tableId;
    private String enTable;
    private String chTable;
    private boolean addToSe;
}

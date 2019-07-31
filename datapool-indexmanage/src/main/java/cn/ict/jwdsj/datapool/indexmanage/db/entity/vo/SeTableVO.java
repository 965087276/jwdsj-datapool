package cn.ict.jwdsj.datapool.indexmanage.db.entity.vo;

import lombok.Data;

@Data
public class SeTableVO {
    private String enDatabase;
    private String chDatabase;
    private long tableId;
    private String enTable;
    private String chTable;
    private boolean sync;
}

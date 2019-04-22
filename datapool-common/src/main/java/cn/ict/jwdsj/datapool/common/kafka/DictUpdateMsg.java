package cn.ict.jwdsj.datapool.common.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 数据字典模块的数据更新消息
 */
@Data
@AllArgsConstructor
public class DictUpdateMsg {

    private DictUpdateType type;
    private long objectId;

    public enum DictUpdateType {
        DATABASE, TABLE, COLUMN;
    }
}

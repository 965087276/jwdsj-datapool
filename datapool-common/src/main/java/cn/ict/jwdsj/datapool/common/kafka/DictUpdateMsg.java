package cn.ict.jwdsj.datapool.common.kafka;

import cn.ict.jwdsj.datapool.common.constant.DictType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 数据字典模块的数据更新消息
 */
@Data
@AllArgsConstructor
public class DictUpdateMsg {

    private DictType type;
    private long objectId;

}

package cn.ict.jwdsj.datapool.dictionary.event;

import cn.ict.jwdsj.datapool.common.constant.DictType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * 字典信息删除事件（发布至数据统计模块）
 */
@Getter
public class DictDeleteEvent extends ApplicationEvent {
    private DictType dictType;

    public DictDeleteEvent(Object source, DictType dictType) {
        super(source);
        this.dictType = dictType;
    }
}

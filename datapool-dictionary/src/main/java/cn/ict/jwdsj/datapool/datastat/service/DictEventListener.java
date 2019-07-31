package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.dictionary.event.DictAddEvent;
import cn.ict.jwdsj.datapool.dictionary.event.DictDeleteEvent;

/**
 * dictionary增加、删除的事件
 */
public interface DictEventListener {

    /**
     * 增加事件
     * @param event
     */
    void addListener(DictAddEvent event);

    /**
     * 删除事件
     * @param event
     */
    void deleteListener(DictDeleteEvent event);

}

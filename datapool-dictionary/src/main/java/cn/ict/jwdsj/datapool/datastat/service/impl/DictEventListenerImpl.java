package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.constant.DictType;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.datastat.service.DictEventListener;
import cn.ict.jwdsj.datapool.datastat.service.StatsColumnService;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import cn.ict.jwdsj.datapool.dictionary.event.DictAddEvent;
import cn.ict.jwdsj.datapool.dictionary.event.DictDeleteEvent;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DictEventListenerImpl implements DictEventListener {

    @Autowired
    private StatsDatabaseService statsDatabaseService;
    @Autowired
    private StatsTableService statsTableService;
    @Autowired
    private StatsColumnService statsColumnService;

    /**
     * 增加事件
     *
     * @param event
     */
    @Override
    @EventListener
    public void addListener(DictAddEvent event) {
        DictType type = event.getDictType();
        switch (type) {
            case DATABASE:
                statsDatabaseService.add(JSON.parseObject(JSON.toJSONString(event.getSource()), DictDatabase.class));
                break;
            case DATABASES:
                break;
            case TABLE:
                break;
            case TABLES:
                break;
            case COLUMN:
                break;
            case COLUMNS:
                break;
        }
    }

    /**
     * 删除事件
     *
     * @param event
     */
    @Override
    @EventListener
    public void deleteListener(DictDeleteEvent event) {

    }
}

package cn.ict.jwdsj.datapool.datastats.service.impl;

import cn.ict.jwdsj.datapool.common.constant.DictType;
import cn.ict.jwdsj.datapool.datastats.service.DictEventListener;
import cn.ict.jwdsj.datapool.datastats.service.StatsColumnService;
import cn.ict.jwdsj.datapool.datastats.service.StatsDatabaseService;
import cn.ict.jwdsj.datapool.datastats.service.StatsTableService;
import cn.ict.jwdsj.datapool.dictionary.event.DictAddEvent;
import cn.ict.jwdsj.datapool.dictionary.event.DictDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DictEventListenerImpl implements DictEventListener {

    @Autowired
    private StatsDatabaseService statsDatabaseService;
    @Autowired
    private StatsTableService statsTableService;
    @Autowired
    private StatsColumnService statsColumnService;

    /**
     * “字典增加”事件
     *
     * @param event
     */
    @Override
    @EventListener
    public void addListener(DictAddEvent event) {
        DictType type = event.getDictType();
        String currentTime = Objects.toString(event.getSource());
        switch (type) {
            case DATABASE:
                // 暂无需求
                break;
            case DATABASES:
                statsDatabaseService.saveAllFromDictDatabase(currentTime);
                break;
            case TABLE:
                // 暂无需求
                break;
            case TABLES:
                statsTableService.saveAllFromDictTable(currentTime);
                break;
            case COLUMN:
                // 暂无需求
                break;
            case COLUMNS:
                statsColumnService.saveAllFromDictColumn(currentTime);
                break;
        }
    }

    /**
     * “字典删除”事件
     *
     * @param event
     */
    @Override
    @EventListener
    public void deleteListener(DictDeleteEvent event) {
        DictType type = event.getDictType();
        long id = parseToLong(event.getSource());
        switch (type) {
            case DATABASE:
                statsDatabaseService.deleteByDatabaseId(id);
                break;
            case DATABASES:
                // 暂无需求
                break;
            case TABLE:
                statsTableService.deleteByTableId(id);
                break;
            case TABLES:
                // 暂无需求
                break;
            case COLUMN:
                statsColumnService.deleteByColumnId(id);
                break;
            case COLUMNS:
                statsColumnService.deleteByTableId(id);
                break;
        }
    }

    private long parseToLong(Object value) {
        return Long.parseLong(String.valueOf(value));
    }
}

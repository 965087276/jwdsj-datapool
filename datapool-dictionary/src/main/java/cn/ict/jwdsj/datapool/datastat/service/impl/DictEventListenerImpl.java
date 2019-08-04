package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.constant.DictType;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
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

import java.util.List;
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

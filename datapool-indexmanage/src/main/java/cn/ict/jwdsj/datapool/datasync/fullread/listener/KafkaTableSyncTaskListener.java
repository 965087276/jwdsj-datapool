package cn.ict.jwdsj.datapool.datasync.fullread.listener;

import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import cn.ict.jwdsj.datapool.datasync.fullread.service.TableFullReadService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@Slf4j
public class KafkaTableSyncTaskListener {
    @Autowired
    private TableFullReadService fullReadService;

    @EventListener
    public void sendSyncMsg(TableSyncMsg msg)  {
        try {
            fullReadService.fullRead(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

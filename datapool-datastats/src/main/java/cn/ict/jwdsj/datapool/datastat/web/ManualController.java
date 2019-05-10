package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.datastat.schedule.StatsScheduleTask;
import cn.ict.jwdsj.datapool.datastat.service.ManualService;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManualController {
    @Autowired
    private ManualService manualService;

    @ApiOperation("手动统计")
    @GetMapping("datastats/manual_sync")
    public ResponseEntity manualSync() {
        manualService.manualSync();
        return ResponseEntity.ok();
    }
    @ApiOperation("手动库统计")
    @GetMapping("datastats/manual_sync/database")
    public ResponseEntity manualSyncDatabase() {
        manualService.manualSyncDatabase();
        return ResponseEntity.ok();
    }
    @ApiOperation("手动表统计")
    @GetMapping("datastats/manual_sync/table")
    public ResponseEntity manualSyncTable() {
        manualService.manualSyncTable();
        return ResponseEntity.ok();
    }
    @ApiOperation("手动字段统计")
    @GetMapping("datastats/manual_sync/column")
    public ResponseEntity manualSyncColumn() {
        manualService.manualSyncColumn();
        return ResponseEntity.ok();
    }
}

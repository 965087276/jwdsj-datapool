package cn.ict.jwdsj.datapool.api.feign;

import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "datapool-datasync")
public interface DataSyncClient {
    @PostMapping("datasync/sync")
    void syncTable(@RequestBody TableSyncMsg msg);
}

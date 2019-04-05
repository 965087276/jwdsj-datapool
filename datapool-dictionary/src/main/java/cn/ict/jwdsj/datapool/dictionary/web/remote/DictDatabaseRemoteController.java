package cn.ict.jwdsj.datapool.dictionary.web.remote;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DictDatabaseRemoteController {
    @Autowired
    private DictDatabaseService dictDatabaseService;

    @GetMapping("remote/dict/database_names")
    public List<DatabaseNameDTO> listNames() {
        return dictDatabaseService.listNames();
    }
}

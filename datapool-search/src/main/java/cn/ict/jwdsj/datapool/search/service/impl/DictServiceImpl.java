package cn.ict.jwdsj.datapool.search.service.impl;

import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.search.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.joining;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictClient dictClient;


    @Override
    @Async
    public Future<List<DictDatabase>> listDatabasesByIds(List<Long> ids) {
        String idStr = getIdStr(ids);
        List<DictDatabase> databases = dictClient.listDatabasesByIds(idStr);
        return new AsyncResult<>(databases);
    }

    @Override
    public List<TableNameDTO> listTableNameDTOByIdIn(List<Long> ids) {
        String idStr = getIdStr(ids);
        return dictClient.listTableNameDTOByIdIn(idStr);
    }

    @Override
    @Async
    public Future<List<ColumnNameDTO>> listColumnNameDTOByTableId(long tableId) {
        List<ColumnNameDTO> list = dictClient.listColumnNameDTOByTableId(tableId);
        return new AsyncResult<>(list);
    }

    private String getIdStr(List<Long> ids) {
        return ids.stream().map(i -> i.toString()).collect(joining(","));
    }
}

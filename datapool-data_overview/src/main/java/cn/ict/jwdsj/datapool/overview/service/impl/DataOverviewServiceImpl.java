package cn.ict.jwdsj.datapool.overview.service.impl;

import cn.ict.jwdsj.datapool.overview.dao.DataOverviceDAO;
import cn.ict.jwdsj.datapool.overview.service.DataOverviewService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataOverviewServiceImpl implements DataOverviewService {

    @Autowired
    private DataOverviceDAO dataOverviceDAO;

    /**
     * 返回某表的数据
     *
     * @param database 库名
     * @param table    表名
     * @param curPage  第几页
     * @param pageSize 每页多少条
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllRecords(String database, String table, int curPage, int pageSize) {
        int from = (curPage-1) * pageSize;
        int size = from + pageSize;
        return dataOverviceDAO.getAllRecords(database, table, from, size);
    }
}

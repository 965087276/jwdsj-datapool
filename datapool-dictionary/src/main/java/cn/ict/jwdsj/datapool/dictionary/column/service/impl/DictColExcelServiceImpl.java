package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.jwdsj.datapool.common.utils.ExcelJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.column.entity.DictColExcelDTO;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColExcelService;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.meta.entity.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaTableService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class DictColExcelServiceImpl implements DictColExcelService {

    @Autowired private DictDatabaseService dictDatabaseService;
    @Autowired private DictTableService dictTableService;
    @Autowired private DictColumnService dictColumnService;
    @Autowired private MetaTableService metaTableService;

    private final String NOT_EXISTS_DATABASE = "数据库不存在，请先加入数据库的中英信息";
    private String NOT_EXISTS_TABLE = "数据库中不存在这个表";
    private final String EMPTY_CHTABLE = "chTable列存在空值";
    private final String DUPLICATE_OBJECT = "存在重复对象";
    private final String EXISTS_IN_DICT_COLUMN = "某些表之前已经加入过dict_column，不能重复加入";

    @Override
    public void saveAll(String enDatabase, File file) {
        DictDatabase dictDatabase = dictDatabaseService.findByEnDatabase(enDatabase);

        // 库名必须在数据库中存在
        assert !BeanUtil.isEmpty(dictDatabase) : NOT_EXISTS_DATABASE;
        // 表头必须正确
        assert ExcelJudgeUtil.judgeHeader(file, DictColExcelDTO.class);

        ExcelReader reader = ExcelUtil.getReader(file);
        List<DictColExcelDTO> colExcelDTOList = reader.readAll(DictColExcelDTO.class);

        // excel中所有表名
        List<String> tableNames =
                colExcelDTOList.stream().map(DictColExcelDTO::getEnTable).collect(Collectors.toList());
        // excel中每个表的所有字段
        Map<String, List<DictColExcelDTO>> excelColsGroupByTable =
                colExcelDTOList.stream().collect(groupingBy(DictColExcelDTO::getEnTable));


        // 中文字段不能为空
        assert colExcelDTOList.stream().map(DictColExcelDTO::getChColumn).allMatch(StrUtil::isNotBlank) : EMPTY_CHTABLE;
        // 不能存在重复记录
        assert colExcelDTOList.size() == colExcelDTOList.stream().distinct().count() : DUPLICATE_OBJECT;
        // 表必须是当前库下的表
        assert allTableExistsInMetaDatabase(dictDatabase.getEnDatabase(), tableNames) : NOT_EXISTS_TABLE;

        // 该表字段必须是第一次导入，即dict_column中不存在这些表
        assert allTableNotExistsInDictColumn(dictDatabase, tableNames) : EXISTS_IN_DICT_COLUMN;
//        // 字段必须是本表的字段
//        assert allColumnsBelongToCorrectTable(excelColsGroupByTable);


    }

    private boolean allTableNotExistsInDictColumn(DictDatabase dictDatabase, List<String> tableNames) {
        return dictTableService.countByDictDatabaseAndEnTableIn(dictDatabase, tableNames) == 0;
    }

    private boolean allTableExistsInMetaDatabase(String enDatabase, List<String> tableNames) {
        Set<String> tableSet = metaTableService.listByDatabase(enDatabase)
                .stream()
                .map(MetaTable::getTable)
                .collect(Collectors.toSet());
        for (String tableName : tableNames) {
            if (!tableSet.contains(tableName)) {
                NOT_EXISTS_TABLE = "数据库中不存在表" + tableName;
                return false;
            }
        }
        return true;
    }
}

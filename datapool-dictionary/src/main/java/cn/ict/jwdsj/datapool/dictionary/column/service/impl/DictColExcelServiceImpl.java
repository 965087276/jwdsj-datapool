package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;
import cn.ict.jwdsj.datapool.common.utils.ExcelJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.column.entity.DictColExcelDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.dictionary.column.mapper.DictColumnMapper;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColExcelService;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.netflix.discovery.converters.Auto;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class DictColExcelServiceImpl implements DictColExcelService {

    @Autowired private DictDatabaseService dictDatabaseService;
    @Autowired private DictTableService dictTableService;
    @Autowired private DictColumnService dictColumnService;
    @Autowired private MetaColumnService metaColumnService;
    @Autowired private DictColumnMapper dictColumnMapper;

    private String NOT_EXISTS_DATABASE = "下列库的字典信息未导入";
    private String NOT_EXISTS_TABLE = "下列表的字典信息未导入";
    private final String EMPTY_CHTABLE = "chTable列存在空值";
    private final String DUPLICATE_OBJECT = "存在重复对象";
    private String EXISTS_IN_DICT_COLUMN = "某些表之前已经加入过dict_column，不能重复加入";
    private String COLUMN_NOT_EXISTS = "所有字段必须真实存在且对应到正确的表上";
    private final String WRONG_TITLE = "表头错误";

//    @Override
//    public void saveAll(long databaseId, MultipartFile file) throws IOException {
//        DictDatabase dictDatabase = dictDatabaseService.findById(databaseId);
//        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
//
//        // 库必须在dict_database中存在
//        Assert.isTrue(!BeanUtil.isEmpty(dictDatabase), NOT_EXISTS_DATABASE);
//        // 表头必须正确
//        Assert.isTrue(ExcelJudgeUtil.judgeHeader(reader.readRow(0), DictColExcelDTO.class), WRONG_TITLE);
//
//        List<DictColExcelDTO> colExcelDTOList = reader.readAll(DictColExcelDTO.class);
//        // excel中所有表名
//        List<String> tableNames =
//                colExcelDTOList.stream().map(DictColExcelDTO::getEnTable).distinct().collect(Collectors.toList());
//        // excel中每个表的所有字段
//        Map<String, List<DictColExcelDTO>> excelColsGroupByTable =
//                colExcelDTOList.stream().collect(groupingBy(DictColExcelDTO::getEnTable));
//        // dict_table中该库的所有表
//        List<TbIdNameDTO> tbIdNameDTOList = dictTableService.listTbIdNameDTOByDictDatabase(dictDatabase);
//
//
//        // 中文字段不能为空
//        Assert.isTrue(colExcelDTOList.stream().map(DictColExcelDTO::getChColumn).allMatch(StrUtil::isNotBlank), EMPTY_CHTABLE);
//        // 不能存在重复记录
//        Assert.isTrue(colExcelDTOList.size() == colExcelDTOList.stream().distinct().count(), DUPLICATE_OBJECT);
//        // 所有表必须先导入到dict_table中
//        Assert.isTrue(allTableExistsInDictTable(tableNames, tbIdNameDTOList), NOT_EXISTS_TABLE);
//        // 该表字段必须是第一次导入，即dict_column中不存在这些表
//        Assert.isTrue(allTableNotExistsInDictColumn(dictDatabase, tableNames), EXISTS_IN_DICT_COLUMN);
//        // 每个字段必须对应到正确的表上
//        Assert.isTrue(allColumnsBelongToCorrectTable(dictDatabase, tableNames, excelColsGroupByTable), COLUMN_NOT_EXISTS);
//
//        List<DictColumn> dictColumnList = mapColExcelsToDictColumns(dictDatabase, colExcelDTOList, tbIdNameDTOList);
//
//        // TIDB中一次事务不能超过5000条，所以大于5000条的要分批插入
//        if (dictColumnList.size() < 5000) {
//            dictColumnService.saveAll(dictColumnList);
//        } else {
//            dictColumnList
//                    .stream()
//                    .collect(groupingBy(dictColumn -> dictColumn.getDictTableId()))
//                    .forEach((tableId, columns) -> {
//                        dictColumnService.saveAll(columns);
//                    });
//        }
//    }

    @Override
    public void saveAll(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());

        // 表头必须正确
        Assert.isTrue(ExcelJudgeUtil.judgeHeader(reader.readRow(0), DictColExcelDTO.class), WRONG_TITLE);
        List<DictColExcelDTO> colExcelDTOList = reader.readAll(DictColExcelDTO.class);

        // Excel数据条数不得超过5000
        Assert.isTrue(colExcelDTOList.size() < 5000, "Excel的数据条数不得超过5000");
        // 不能存在重复记录
        Assert.isTrue(colExcelDTOList.size() == colExcelDTOList.stream().distinct().count(), DUPLICATE_OBJECT);
        // 中文字段不能为空
        Assert.isTrue(colExcelDTOList.stream().map(DictColExcelDTO::getChColumn).allMatch(StrUtil::isNotBlank), EMPTY_CHTABLE);

        // Excel中的所有库
        List<String> excelDatabases = colExcelDTOList.stream().map(DictColExcelDTO::getEnDatabase).distinct().collect(Collectors.toList());
        // 所有库必须先导入到dict_database中
        Assert.isTrue(allDatabaseExistsInDictDatabase(excelDatabases), NOT_EXISTS_DATABASE);

        // Excel中的所有库
        Map<String, DictDatabase> excelDictDatabases = excelDatabases.parallelStream()
                .map(dictDatabaseService::findByEnDatabase)
                .collect(toMap(DictDatabase::getEnDatabase, dictDatabase -> dictDatabase));

        // Excel中的所有表
        Map<String, Set<String>> excelTables = colExcelDTOList.stream()
                .collect(groupingBy(DictColExcelDTO::getEnDatabase,
                        mapping(DictColExcelDTO::getEnTable, toSet())));

        // Excel中的所有字段(库、表、字段列表）
        Map<String, Map<String, Set<String>>> excelColumns = colExcelDTOList.stream()
                .collect(groupingBy(DictColExcelDTO::getEnDatabase,
                            groupingBy(DictColExcelDTO::getEnTable,
                                    mapping(DictColExcelDTO::getEnColumn, toSet()))));




        // 所有表必须先导入到dict_table中
        Assert.isTrue(allTableExistsInDictTable(excelTables, excelDictDatabases), NOT_EXISTS_TABLE);

        // 每个字段必须对应到正确的表上
        Assert.isTrue(allColumnsBelongToCorrectTable(excelColumns), COLUMN_NOT_EXISTS);

        // excel中的所有表, map的key为 库名 + "." + 表名
        Map<String, DictTable> excelDictTables = colExcelDTOList.stream()
                .map(col -> new MetaTable(col.getEnDatabase(), col.getEnTable()))
                .distinct()
                .parallel()
                .map(p -> dictTableService.findByEnDatabaseAndEnTable(p.getDatabase(), p.getTable()))
                .collect(toMap(t -> t.getEnDatabase() + "." + t.getEnTable(), t -> t));

        List<DictColumn> dictColumnList = colExcelDTOList.stream()
                .map(col -> DictColumn.builder()
                        .dictDatabaseId(excelDictDatabases.get(col.getEnDatabase()).getId())
                        .dictTableId(excelDictTables.get(col.getEnDatabase() + "." + col.getEnTable()).getId())
                        .enDatabase(col.getEnDatabase())
                        .enTable(col.getEnTable())
                        .enColumn(col.getEnColumn())
                        .chColumn(col.getChColumn())
                        .build()
                ).collect(toList());
        dictColumnMapper.insertIgnore(dictColumnList);

    }

    /**
     * 每个字段必须对应到正确的表上
     * @param excelColumns
     * @return
     */
    private boolean allColumnsBelongToCorrectTable(Map<String, Map<String, Set<String>>> excelColumns) {
        COLUMN_NOT_EXISTS = "下列表的字段有误: ";
        // vector线程安全
        Vector<String> notAdd = new Vector<>();
        excelColumns.keySet().parallelStream().forEach(database -> {
            // 待判断的字段
            Map<String, Set<String>> tbAndColsTest = excelColumns.get(database);
            List<String> tables = tbAndColsTest.keySet().stream().collect(toList());
            // 数据库中真实的字段
            Map<String, Set<String>> tbAndColsReal = metaColumnService.listByDatabaseAndTableIn(database, tables)
                    .stream()
                    .collect(groupingBy(MetaColumn::getTable, mapping(MetaColumn::getColumn, toSet())));
            // 待判断的字段的集合与真实字段的集合取并集，结果应该等于原来“真实字段的集合”的大小
            tbAndColsReal.forEach((table, columnsRealSet) -> {
                Set<String> columnsTestSet = tbAndColsTest.get(table);
                int oldSize = columnsRealSet.size();
                columnsRealSet.addAll(columnsTestSet);
                int newSize = columnsRealSet.size();
                if (oldSize != newSize) {
                    notAdd.add(database + "." + table);
                }
            });
        });
        COLUMN_NOT_EXISTS += notAdd.stream().collect(joining(","));
        return notAdd.isEmpty();
    }


    /**
     * 判断库是否已加入dict_database
     * @param excelDatabases
     * @return
     */
    private boolean allDatabaseExistsInDictDatabase(List<String> excelDatabases) {
        List<String> notAdd = excelDatabases.parallelStream()
                .filter(database -> !dictDatabaseService.exists(database))
                .collect(toList());
        NOT_EXISTS_DATABASE += notAdd.stream().collect(joining(","));
        return notAdd.isEmpty();
    }

    /**
     * 判断表是否已加入dict_table
     * @param excelTables
     * @param excelDictDatabases
     * @return
     */
    private boolean allTableExistsInDictTable(Map<String, Set<String>> excelTables, Map<String, DictDatabase> excelDictDatabases) {
        List<String> notAdd = new ArrayList<>();
        excelTables.forEach((database, tables) -> {
            Set<String> tablesInDict = dictTableService.listEnTablesByDictDatabase(excelDictDatabases.get(database)).stream().collect(toSet());
            tables.forEach(table -> {
                if (!tablesInDict.contains(table)) {
                    notAdd.add(database + "." + table);
                }
            });
        });

        NOT_EXISTS_TABLE += notAdd.stream().collect(joining(","));
        return notAdd.isEmpty();
    }

    private DictColumn getDictColByColExcelAndTableId(DictColExcelDTO colExcelDTO, TbIdNameDTO tbIdNameDTO, DictDatabase dictDatabase) {
        DictColumn dictColumn = BeanUtil.toBean(colExcelDTO, DictColumn.class);
        dictColumn.setDictTableId(tbIdNameDTO.getId());
        dictColumn.setEnTable(tbIdNameDTO.getEnTable());
        dictColumn.setDictDatabaseId(dictDatabase.getId());
        dictColumn.setEnDatabase(dictDatabase.getEnDatabase());
        return dictColumn;
    }



//    private boolean allTableNotExistsInDictColumn(DictDatabase dictDatabase, List<String> tableNames) {
//        EXISTS_IN_DICT_COLUMN = "下列表之前已经导入过，excel不支持再次导入: ";
//        List<String> wrongTables = new ArrayList<>();
//        Set<String> tablesInDictColumnByDatabase = new HashSet<>(
//                dictColumnService.getEnTableByDictDatabaseId(dictDatabase.getId())
//        );
//        for (String tableName : tableNames) {
//            if (tablesInDictColumnByDatabase.contains(tableName)) {
//                wrongTables.add(tableName);
//            }
//        }
//        EXISTS_IN_DICT_COLUMN += wrongTables.stream().collect(Collectors.joining(","));
//        return wrongTables.isEmpty();
//    }

}

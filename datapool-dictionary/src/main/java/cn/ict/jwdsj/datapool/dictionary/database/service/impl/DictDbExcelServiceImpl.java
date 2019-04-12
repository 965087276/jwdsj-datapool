package cn.ict.jwdsj.datapool.dictionary.database.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.jwdsj.datapool.common.utils.ExcelJudgeUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.entity.dto.DictDbExcelDTO;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDbExcelService;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictDbExcelServiceImpl implements DictDbExcelService {
    @Autowired
    private DictDatabaseService dictDatabaseService;
    @Autowired
    private MetaDatabaseService metaDatabaseService;

    private final String WRONG_HEADER = "表头错误";
    private final String WRONG_ENDATABASE = "enDatabase列的元素有误";
    private final String EMPRY_CHDATABASE = "chDatabase列存在空值";
    private final String DUPLICATE_OBJECT = "存在重复对象";
    private final String EXISTS_OBJECT = "库中已存在某enDatabase";

    @Override
    @Transactional
    public void saveByExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 判断表头
        Assert.isTrue(ExcelJudgeUtil.judgeHeader(reader.readRow(0), DictDatabase.class), WRONG_HEADER);
        // 获取excel内容
        List<DictDbExcelDTO> dictDatabaseDTOs = reader.readAll(DictDbExcelDTO.class);

        // chDatabase字段不能为空
        Assert.isTrue(dictDatabaseDTOs.stream().map(DictDbExcelDTO::getChDatabase).allMatch(StrUtil::isNotBlank), EMPRY_CHDATABASE);

        // excel中不能有重复记录
        Assert.isTrue(dictDatabaseDTOs.size() == dictDatabaseDTOs.stream().distinct().count(), DUPLICATE_OBJECT);

        // 数据库必须是真实存在的库
        Assert.isTrue(dictDatabaseDTOs.stream().map(DictDbExcelDTO::getEnDatabase).allMatch(metaDatabaseService::exists), WRONG_ENDATABASE);

        // dict_database表中不能有这个库
        Assert.isTrue(dictDatabaseDTOs.stream().map(DictDbExcelDTO::getEnDatabase).noneMatch(dictDatabaseService::exists), EXISTS_OBJECT);

        List<DictDatabase> dictDatabases = dictDatabaseDTOs
                .stream()
                .map(dictDbExcelDTO -> BeanUtil.toBean(dictDbExcelDTO, DictDatabase.class))
                .collect(Collectors.toList());

        dictDatabaseService.saveAll(dictDatabases);
    }

}

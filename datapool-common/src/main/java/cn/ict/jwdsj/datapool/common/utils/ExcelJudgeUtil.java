package cn.ict.jwdsj.datapool.common.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcelJudgeUtil {

    /**
     * 判断excel的表头是否与指定实体类的字段名一致
     * excel表头的字段必须都在实体类中存在
     * @param excelFile
     * @param clazz
     * @return
     */
    public static boolean judgeHeader(File excelFile, Class<?> clazz) {

        ExcelReader reader = ExcelUtil.getReader(excelFile);

        List<String> header = reader.readRow(0).stream().map(Object::toString).collect(Collectors.toList());
        Set<String> fields = Arrays.stream(ClassUtil.getDeclaredFields(clazz)).map(Field::getName).collect(Collectors.toSet());


        return header.stream().allMatch(fields::contains) && header.size() == fields.size() ? true : false;


    }
}

package cn.ict.jwdsj.datapool.dictionary.service.dict.column.service;

import cn.ict.jwdsj.datapool.dictionary.column.service.DictColExcelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictColExcelServiceTest {
    @Autowired private DictColExcelService colExcelService;

    @Test
    public void saveAllTest() {
        File file = new File("C:\\Users\\wangjinhao\\Desktop\\data poo测试\\dict_column.xlsx");
        String enDatabase = "zkbh-test";
        colExcelService.saveAll(enDatabase, file);
    }

    @Test
    public void test() {
        int N = 3;
        List<Integer> integerList = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
        List<List<Integer>> list = IntStream.range(0, 4)
                .mapToObj(i -> integerList.subList(i * N, Math.min(i * N + N, integerList.size())))
                .collect(Collectors.toList());
        list.forEach(System.out::println);
    }
}

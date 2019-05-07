package cn.ict.jwdsj.datapool.dictionary.column.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface DictColExcelService {

//    void saveAll(String enDatabase, File file);

//    void saveAll(long databaseId, MultipartFile file) throws IOException;

    void saveAll(MultipartFile file) throws IOException;
}

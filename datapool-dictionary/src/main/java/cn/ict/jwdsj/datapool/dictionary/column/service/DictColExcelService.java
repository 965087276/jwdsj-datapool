package cn.ict.jwdsj.datapool.dictionary.column.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface DictColExcelService {

//    void saveAllToDb(String enDatabase, File file);

//    void saveAllToDb(long databaseId, MultipartFile file) throws IOException;

    void saveAll(MultipartFile file) throws IOException;
}

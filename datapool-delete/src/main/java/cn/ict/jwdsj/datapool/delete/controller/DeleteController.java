package cn.ict.jwdsj.datapool.delete.controller;

import cn.ict.jwdsj.datapool.delete.service.DeleteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DeleteController {
    @Autowired
    private DeleteService deleteService;

    @ApiOperation(value = "批量删除不存在的库、表、字段")
    @DeleteMapping("/delete")
    public String delete() throws IOException {
        deleteService.scheduledDelete();
        return "正在删除";
    }
}

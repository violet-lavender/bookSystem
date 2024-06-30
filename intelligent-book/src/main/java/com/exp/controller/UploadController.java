package com.exp.controller;

import com.exp.pojo.Result;
import com.exp.utils.OBSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class UploadController {
    // 注入华为云obs工具类
    @Autowired
    private OBSUtils obsUtils;

    // 文件上传时触发请求
    @PostMapping("/upload")
    public Result upload(@RequestParam("image") MultipartFile image) throws IOException {
        log.info("文件上传, 文件名: {}", image.getOriginalFilename());

        // 调用华为云OBS工具类进行文件上传
        String url = obsUtils.upload(image);
        log.info("文件上传完成, 文件访问的url: {}", url);

        return Result.success(url);
    }
}

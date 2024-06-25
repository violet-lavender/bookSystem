package com.exp.utils;

import com.obs.services.ObsClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

// 华为云OBS工具类
@Component  // 交给IOC容器来管理, 注意Utils不属于Controller、Service、Mapper(Dao)层中的任意一层
public class OBSUtils {
    private final String ak = System.getenv("OBS_ACCESS_KEY_ID");
    private final String sk = System.getenv("OBS_SECRET_ACCESS_KEY_ID");
    private final String endPoint = "https://obs.cn-north-4.myhuaweicloud.com";
    private final String bucketName = "web-exp";

    public String upload(MultipartFile file) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖, uuid + 文件拓展名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OBS
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        obsClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endPoint.split("//")[0] + "//" + bucketName + "." + endPoint.split("//")[1] + "/" + fileName;
        // 关闭obsClient
        obsClient.close();
        // 把上传到obs的路径返回
        return url;
    }
}

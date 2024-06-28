package com.exp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.exp.dto.RecommendParam;
import com.exp.mapper.UserMapper;
import com.exp.pojo.Book;
import com.exp.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class PythonService {

    @Autowired
    private UserMapper userMapper;

    // TODO Recommend books for user
    public Result booksRecommendForUser(RecommendParam obj){
        String pythonEnvPath = "python";
        String pythonScriptPath = "";

        try{
            String jsonString = JSON.toJSONString(obj);
            String[] cmd = new String[]{pythonEnvPath, pythonScriptPath, jsonString};

            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process p = pb.start();

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = input.readLine())!= null) {
                output.append(line);
            }
            p.waitFor();

            List<Integer> bookIds = JSON.parseObject(output.toString(), new TypeReference<List<Integer>>() {});

            List<Book> bookList = userMapper.bookListByIds(bookIds);

            return Result.success(bookList);

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}



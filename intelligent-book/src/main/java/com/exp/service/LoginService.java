package com.exp.service;

import com.exp.dto.RegisterRequest;
import com.exp.mapper.LoginMapper;
import com.exp.pojo.Admin;
import com.exp.pojo.Analyst;
import com.exp.pojo.Result;
import com.exp.pojo.User;
import com.exp.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private LoginMapper loginMapper;

    // 可以借助Spring Security中的BCryptPasswordEncoder进行密码加密, 或者自定义哈希算法加密, 这里不做要求
    public Result login(String username, String password, String role) {
        // 这个函数代码冗余度很高, 懒得修改了
        switch (role.toLowerCase()) {
            case "admin":
                Admin admin = loginMapper.getAdmin(username, password);
                if (admin == null) {
                    return Result.error("Invalid username or password");
                }else {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", role);
                    claims.put("id", admin.getId());
                    claims.put("name", admin.getUsername());
                    return Result.success(JwtUtils.generateJwt(claims));
                }

            case "user":
                User user = loginMapper.getUser(username, password);
                if (user == null) {
                    return Result.error("Invalid username or password");
                }else {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", role);
                    claims.put("id", user.getId());
                    claims.put("name", user.getUsername());

                    return Result.success(JwtUtils.generateJwt(claims));
                }

            case "analyst":
                Analyst analyst = loginMapper.getAnalyst(username, password);
                if (analyst == null) {
                    return Result.error("Invalid username or password");
                }else {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", role);
                    claims.put("id", analyst.getId());
                    claims.put("name", analyst.getUsername());

                    return Result.success(JwtUtils.generateJwt(claims));
                }

            default:
                return Result.error("Invalid role");
        }
    }

    public Result registerUser(RegisterRequest registerRequest) {

    }
}

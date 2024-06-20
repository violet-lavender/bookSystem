package com.exp.service;

import com.exp.dto.RegisterRequest;
import com.exp.mapper.LoginMapper;
import com.exp.pojo.Admin;
import com.exp.pojo.Analyst;
import com.exp.pojo.Result;
import com.exp.pojo.User;
import com.exp.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 可以借助Spring Security中的BCryptPasswordEncoder进行密码加密, 或者自定义哈希算法加密, 这里不做要求
    public Result login(String username, String password, String role) {
        // 这个函数代码冗余度很高, 烂完了, 但是懒得修改
        switch (role.toLowerCase()) {
            case "admin":
                Admin admin = loginMapper.getAdminByUsername(username);
                if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
                    return Result.error("Invalid username or password");
                } else {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", role);
                    claims.put("id", admin.getId());
                    claims.put("username", admin.getUsername());
                    return Result.success(JwtUtils.generateJwt(claims));
                }

            case "user":
                User user = loginMapper.getUserByUsername(username);
                if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
                    return Result.error("Invalid username or password");
                } else if (user.getIsEnabled() == 0) {
                    return Result.error("You have been added to the blacklist and cannot log in");
                } else {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", role);
                    claims.put("id", user.getId());
                    claims.put("username", user.getUsername());

                    return Result.success(JwtUtils.generateJwt(claims));
                }

            case "analyst":
                Analyst analyst = loginMapper.getAnalystByUsername(username);
                if (analyst == null || !passwordEncoder.matches(password, analyst.getPassword())) {
                    return Result.error("Invalid username or password");
                } else {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", role);
                    claims.put("id", analyst.getId());
                    claims.put("username", analyst.getUsername());

                    return Result.success(JwtUtils.generateJwt(claims));
                }

            default:
                return Result.error("Invalid role");
        }
    }

    public Result registerUser(RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null) {
            return Result.error("All fields are required.");
        }
        try {
            String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
            registerRequest.setPassword(hashedPassword);
            loginMapper.insertUser(registerRequest);
            return Result.success();
        } catch (DataIntegrityViolationException e) {
            // 捕获唯一约束违规异常
            return Result.error("Username already exists.");
        } catch (Exception e) {
            // 捕获其他异常
            return Result.error(e.getMessage());
        }
    }
}

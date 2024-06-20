package com.exp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Integer gender; // 1 男, 2 女
    private String image;
    private Integer age;
    private String address;
    private String phone;
    private String email;
    private Integer lendFrequency;
    private Integer disFrequency;
    private Integer isEnabled; // 0: 禁用, 1: 启用
    private LocalDateTime blacklistUntil;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
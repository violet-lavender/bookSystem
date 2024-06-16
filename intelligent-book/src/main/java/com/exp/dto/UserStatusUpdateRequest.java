package com.exp.dto;

import lombok.Data;

@Data
public class UserStatusUpdateRequest {
    private Integer id;
    private Integer isEnabled;  // 0: 禁用, 1: 启用
}
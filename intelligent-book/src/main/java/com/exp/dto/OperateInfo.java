package com.exp.dto;

import lombok.Data;

@Data
public class OperateInfo {
    private String operateRole; //操作人角色
    private Integer operateUser; //操作人ID
    private String operateUsername; //操作人用户名
}

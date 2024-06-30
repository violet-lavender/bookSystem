package com.exp.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Assess {
    private String userName;
    private Integer grade;
    private String assess;
    private LocalDateTime updateTime;
}

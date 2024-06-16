package com.exp.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lend {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private Integer duration; // 借阅时长，默认15天
    private LocalDate lendDate;
    private LocalDate backDate;
    private Integer isBack; // 0: 否, 1: 是
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
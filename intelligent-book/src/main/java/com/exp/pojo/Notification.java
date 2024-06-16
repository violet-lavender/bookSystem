package com.exp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Integer id;
    private Integer userId;
    private String message;
    private Integer isRead; // 0: 未读, 1: 已读
    private Integer isVisual;   // 0: 不显示, 1: 显示
    private LocalDateTime createTime;
}

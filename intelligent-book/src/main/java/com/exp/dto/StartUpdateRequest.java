package com.exp.dto;

import lombok.Data;

@Data
public class StartUpdateRequest {
    private Integer userId;
    private Integer bookId;
    private Integer isLike; // 0: 取消点赞(收藏), 1: 点赞(收藏)
}

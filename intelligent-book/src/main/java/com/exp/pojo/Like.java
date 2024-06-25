package com.exp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private String bookName;
    private String bookAuthor;
    private String bookClass;
    private LocalDateTime createTime;
}

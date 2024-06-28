package com.exp.pojo;

import com.exp.vo.Assess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;
    private String name;
    private String author;
    private String press;
    private String isbn;
    private String image;
    private String introduction;
    private String language;
    private Double price;
    private Date pubDate;
    private Integer classId;
    private String className;
    private Integer number;
    private Integer lendFrequency;
    private Integer isLike; // 点赞(收藏)信息, 0: 未点赞, 1: 已点赞
    private Integer stars;
    private Integer grade;
    private List<Assess> assessList;
    private List<String> tagList;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package com.exp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;
    private String name;
    private String author;
    private String press;
    private String ISBN;
    private String image;
    private String introduction;
    private String language;
    private Double price;
    private Date pubDate;
    private Integer classId;
    private Integer number;
    private Integer lendFrequency;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

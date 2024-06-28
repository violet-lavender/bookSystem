package com.exp.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendParam {
    private Integer userId;
    private List<BookGrade> bookGradeList;
    private List<Integer> bookIds;
}


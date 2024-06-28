package com.exp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookGrade {
    private Integer userId;
    private Integer bookId;
    private Integer grade;
}

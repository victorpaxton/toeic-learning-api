package com.tma.english.models.dto.test.part7;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionP7DTO {
    private Long id;
    private int questionNo;
    private int numOfPassages;
    private String passageText;
    private String passageType;
    private String image;
    private List<QuestionP7DTO> questionList;
//    private TestOverviewDTO testOverviewDTO;
}

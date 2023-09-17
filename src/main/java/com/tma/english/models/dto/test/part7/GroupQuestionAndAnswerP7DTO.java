package com.tma.english.models.dto.test.part7;

import com.tma.english.models.dto.test.TestOverviewDTO;
import com.tma.english.models.dto.test.part6.QuestionAndAnswerP6DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionAndAnswerP7DTO {
    private Long id;
    private int questionNo;
    private int numOfPassages;
    private String passageText;
    private String passageType;
    private String image;
    private String inVietnamese;
    private List<QuestionAndAnswerP7DTO> questionList;
    private TestOverviewDTO testOverviewDTO;
}

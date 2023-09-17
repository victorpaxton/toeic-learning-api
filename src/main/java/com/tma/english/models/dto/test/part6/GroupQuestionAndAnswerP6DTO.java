package com.tma.english.models.dto.test.part6;

import com.tma.english.models.dto.test.TestOverviewDTO;
import com.tma.english.models.dto.test.part4.QuestionAndAnswerP4DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionAndAnswerP6DTO {
    private Long id;
    private int questionNo;
    private String passageText;
    private String passageType;
    private String inVietnamese;
    private List<QuestionAndAnswerP6DTO> questionList;
    private TestOverviewDTO testOverviewDTO;
}

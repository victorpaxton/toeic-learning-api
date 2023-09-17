package com.tma.english.models.dto.test.part6;

import com.tma.english.models.dto.test.TestOverviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionP6DTO {
    private Long id;
    private int questionNo;
    private String passageText;
    private String passageType;
    private List<QuestionP6DTO> questionList;
//    private TestOverviewDTO testOverviewDTO;
}

package com.tma.english.models.dto.test.part4;

import com.tma.english.models.dto.test.TestOverviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionAndAnswerP4DTO {
    private Long id;
    private int questionNo;
    private String audio;
    private String image;
    private String transcript;
    private String vietnameseTranscript;
    private List<QuestionAndAnswerP4DTO> questionList;
    private TestOverviewDTO testOverviewDTO;
}

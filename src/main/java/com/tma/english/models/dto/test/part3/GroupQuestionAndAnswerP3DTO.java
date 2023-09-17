package com.tma.english.models.dto.test.part3;

import com.tma.english.models.dto.test.TestOverviewDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionAndAnswerP3DTO {
    private Long id;
    private int questionNo;
    private String audio;
    private String image;
    private String transcript;
    private String vietnameseTranscript;
    private List<QuestionAndAnswerP3DTO> questionList;
    private TestOverviewDTO testOverviewDTO;
}

package com.tma.english.models.dto.test.part4;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupQuestionP4CreateDTO {
//    private String audio;
//    private String image;
    @Min(value = 71)
    @Max(value = 100)
    private int questionNo;

    private String transcript;
    private String vietnameseTranscript;

    private List<QuestionP4CreateDTO> questionsList;
}

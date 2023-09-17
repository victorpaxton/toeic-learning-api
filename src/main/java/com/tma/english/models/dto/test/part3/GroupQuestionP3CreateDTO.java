package com.tma.english.models.dto.test.part3;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupQuestionP3CreateDTO {

//    private String audio;
//    private String image;
    @Min(value = 32)
    @Max(value = 70)
    private int questionNo;
    private String transcript;
    private String vietnameseTranscript;

    private List<QuestionP3CreateDTO> questionsList;

}

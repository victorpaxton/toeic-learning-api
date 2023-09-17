package com.tma.english.models.dto.test.part6;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionP6CreateDTO {
    @Min(value = 131)
    @Max(value = 146)
    private int questionNo;

    private String passageText;
    private String passageType;
    private String inVietnamese;

    private List<QuestionP6CreateDTO> questionsList;

}

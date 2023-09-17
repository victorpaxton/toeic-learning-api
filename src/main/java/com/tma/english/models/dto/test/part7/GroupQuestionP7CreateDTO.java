package com.tma.english.models.dto.test.part7;

import com.tma.english.models.dto.test.part6.QuestionP6CreateDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupQuestionP7CreateDTO {
    @Min(value = 147)
    @Max(value = 200)
    private int questionNo;

    private int numOfPassages;
    private String passageText;
    private String passageType;

    private String image;
    private String inVietnamese;

    private List<QuestionP7CreateDTO> questionsList;
}

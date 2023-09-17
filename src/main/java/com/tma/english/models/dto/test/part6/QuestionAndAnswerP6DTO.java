package com.tma.english.models.dto.test.part6;

import com.tma.english.models.entities.test.GroupQuestionPartSix;
import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionAndAnswerP6DTO {

    private Long id;

    private int questionNo;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    private int correctAnswer;

    private String explanationAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;


}

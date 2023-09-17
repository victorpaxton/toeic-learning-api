package com.tma.english.models.dto.test.part3;

import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionAndAnswerP3DTO {
    private Long id;

    private int questionNo;
    private String questionText;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    private int correctAnswer;

    private String explanationAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}

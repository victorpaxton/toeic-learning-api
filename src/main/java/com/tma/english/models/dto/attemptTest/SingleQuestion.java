package com.tma.english.models.dto.attemptTest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleQuestion {
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

    private int userAnswer;
    private boolean isCorrect;
}

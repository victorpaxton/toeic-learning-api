package com.tma.english.models.dto.test.part7;

import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionP7CreateDTO {
    @Min(value = 147)
    @Max(value = 200)
    private int questionNo;

    private String questionText;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    @Min(value = 1)
    @Max(value = 4)
    private int correctAnswer;

    private String explanationAnswer;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}

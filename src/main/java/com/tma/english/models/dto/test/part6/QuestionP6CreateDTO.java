package com.tma.english.models.dto.test.part6;

import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionP6CreateDTO {
    @Min(value = 131)
    @Max(value = 146)
    private int questionNo;

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

package com.tma.english.models.dto.test.part7;

import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionP7DTO {
    private Long id;
    private int questionNo;

    private String questionText;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}

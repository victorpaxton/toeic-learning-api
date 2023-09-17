package com.tma.english.models.dto.test.part5;

import com.tma.english.models.dto.test.TestOverviewDTO;
import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionAndAnswerP5DTO {
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

    private TestOverviewDTO testOverviewDTO;
}

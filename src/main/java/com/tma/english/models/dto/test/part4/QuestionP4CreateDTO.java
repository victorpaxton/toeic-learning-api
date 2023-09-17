package com.tma.english.models.dto.test.part4;

import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionP4CreateDTO {
    @Min(value = 71)
    @Max(value = 100)
    private int questionNo;

    @Schema(example = "Which of the following is true?")
    private String questionText;

    @Schema(example = "right")
    private String option1;
    @Schema(example = "left")
    private String option2;
    @Schema(example = "top")
    private String option3;
    @Schema(example = "bot")
    private String option4;

    @Min(value = 1)
    @Max(value = 4)
    private int correctAnswer;

    private String explanationAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}

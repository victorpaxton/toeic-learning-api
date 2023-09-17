package com.tma.english.models.dto.test.part3;

import com.tma.english.models.entities.test.GroupQuestionPartThree;
import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionP3CreateDTO {
    @Min(value = 32)
    @Max(value = 70)
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

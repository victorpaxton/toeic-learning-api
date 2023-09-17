package com.tma.english.models.dto.test.part2;

import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionP2CreateDTO {
//    @NotBlank(message = "This type of question must have an audio")
//    @Schema(example = "audio url")
//    private String audio;
    @Min(value = 7)
    @Max(value = 31)
    private int questionNo;

    @Min(value = 1, message = "Question have 3 choices")
    @Max(value = 3, message = "Question have 3 choices")
    private int correctAnswer;

    @Schema(example = "answer explanation...")
    private String explanationAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}

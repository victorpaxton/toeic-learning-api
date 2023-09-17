package com.tma.english.models.dto.test.part2;

import com.tma.english.models.dto.test.TestOverviewDTO;
import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionAndAnswerP2DTO {
    @Schema(example = "1")
    private Long id;
    private int questionNo;
    @Schema(example = "audio url")
    private String audio;
    @Schema(example = "3")
    private int correctAnswer;
    @Schema(example = "answer explanation")
    private String explanationAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    private TestOverviewDTO testOverviewDTO;
}

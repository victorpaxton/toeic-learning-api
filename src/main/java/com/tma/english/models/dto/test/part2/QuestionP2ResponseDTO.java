package com.tma.english.models.dto.test.part2;

import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionP2ResponseDTO {
    @Schema(example = "1")
    private Long id;

    private int questionNo;

    @Schema(example = "audio url")
    private String audio;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}

package com.tma.english.models.dto.test.part1;

import com.tma.english.models.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuestionP1CreateDTO {
//    @NotBlank(message = "This type of question must have an audio")
//    @Schema(example = "audio url")
//    private String audio;
//
//    @NotBlank(message = "This type of question must have an image")
//    @Schema(example = "image url")
//    private String image;

//    private MultipartFile audio;
//    private MultipartFile image;
    @Min(value = 1)
    @Max(value = 6)
    private int questionNo;

    @Min(value = 1, message = "Question have 4 choices")
    @Max(value = 4, message = "Question have 4 choices")
    private int correctAnswer;

    @Schema(example = "answer explanation")
    private String explanationAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}

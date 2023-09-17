package com.tma.english.models.dto.test.part5;

import com.tma.english.models.entities.test.Test;
import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuestionP5CreateDTO {
    @Min(value = 101)
    @Max(value = 130)
    private int questionNo;

    @Column(name = "question_text")
    private String questionText;

    private String option1;

    private String option2;

    private String option3;

    private String option4;

    @Column(name = "correct_answer")
    private int correctAnswer;

    @Column(name = "explanation_answer")
    private String explanationAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

}

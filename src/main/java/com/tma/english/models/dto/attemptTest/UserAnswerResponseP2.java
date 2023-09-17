package com.tma.english.models.dto.attemptTest;

import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAnswerResponseP2 {
    private Long id;
    private int questionNo;
    private String audio;
    private int correctAnswer;
    private String explanationAnswer;
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    private int userAnswer;
    private boolean isCorrect;
}

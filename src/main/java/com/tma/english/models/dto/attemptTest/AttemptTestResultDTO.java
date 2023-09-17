package com.tma.english.models.dto.attemptTest;

import com.tma.english.models.dto.StreakResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttemptTestResultDTO {

    private Long id;

    private int listeningCorrect;
    private int readingCorrect;

    private int listeningScore;
    private int readingScore;

    private int totalScore;

    private Instant completedAt;
    private int attemptNo;

    private StreakResponseDTO streakStatus;

}

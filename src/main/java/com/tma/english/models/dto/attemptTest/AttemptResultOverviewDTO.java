package com.tma.english.models.dto.attemptTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttemptResultOverviewDTO {
    private int totalScore;

    private int part1Correct;
    private int part2Correct;
    private int part3Correct;
    private int part4Correct;
    private int part5Correct;
    private int part6Correct;
    private int part7Correct;

    private int listeningScore;
    private int readingScore;
}

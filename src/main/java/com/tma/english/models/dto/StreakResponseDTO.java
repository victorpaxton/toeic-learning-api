package com.tma.english.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StreakResponseDTO {
    private GoalStatus vocabStatus;
    private GoalStatus testStatus;
    private Boolean achiveStreakToday;
    private Integer streakLength;

    @Data
    @Builder
    public static class GoalStatus {
        private Integer goal;
        private Long learned;
        private Boolean achieved;
    }
}

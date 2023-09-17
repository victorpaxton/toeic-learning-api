package com.tma.english.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGoalDTO {
    private Integer vocabDailyGoal;
    private Integer testDailyGoal;
}

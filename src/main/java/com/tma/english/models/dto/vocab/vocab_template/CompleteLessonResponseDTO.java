package com.tma.english.models.dto.vocab.vocab_template;

import com.tma.english.models.dto.StreakResponseDTO;
import com.tma.english.models.dto.vocab.VocabCategoryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteLessonResponseDTO {

    private Long scoreEarn;

    private VocabCategoryResponseDTO categorySummaryStats;

    private StreakResponseDTO streakStatus;

}

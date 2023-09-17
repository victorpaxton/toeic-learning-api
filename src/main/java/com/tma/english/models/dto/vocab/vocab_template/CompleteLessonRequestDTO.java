package com.tma.english.models.dto.vocab.vocab_template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteLessonRequestDTO {

    private Long scoreEarned;

    private List<JustLearned> justLearnedList;

    @Data
    public static class JustLearned {
        private Long vocabularyId;
        private Boolean isDifficult;
        private Boolean ignored;
    }


}

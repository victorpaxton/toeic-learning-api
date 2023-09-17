package com.tma.english.models.dto.vocab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VocabCategoryResponseDTO {
    private Long id;
    private String categoryName;
    private String image;
    private Long totalWords;

    private Status learningStatus;

    @Data
    @Builder
    public static class Status {
        private Long learned;
        private Long difficult;
        private Long ignored;
        private Integer completedPercent;
        private Boolean completedThisSession;
    }
}

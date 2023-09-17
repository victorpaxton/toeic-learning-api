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
public class VocabMultipleChoiceDTO {
    private Long id;
    private String template;
    private Prompt prompt;
    private String answer;
    private List<String> choices;

    @Data
    @Builder
    public static class Prompt {
        private String reversedAnswer;
        private String audio;
        private String video;
        private String image;
        private String pronunciation;
        private List<String> attributes;
    }


}

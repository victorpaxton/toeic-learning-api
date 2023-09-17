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
public class LearnTemplateResponseDTO {
    private Long id;
    private String english;
    private String definition;
    private VocabPresentationDTO presentation;
    private List<VocabMultipleChoiceDTO> screens;
}

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
public class VocabPresentationDTO {
    private Long id;
    private String template;
    private String english;
    private String definition;
    private String pronunciation;
    private List<String> attributes;
    private String audio;
    private String video;
    private String image;
}

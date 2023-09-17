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
public class VocabTemplateCreateDTO {
    private List<String> englishChoices;

    private List<String> vietnameseChoices;

    private List<String> letterChoices;
}

package com.tma.english.models.dto.vocab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVocabResponseDTO {
    private Long id;
    private String english;
    private String vietnamese;
    private String audio;
    private String video;
    private String image;
    private String pronunciation;
    private String attributes;

    private UserVocabStatus status;

}

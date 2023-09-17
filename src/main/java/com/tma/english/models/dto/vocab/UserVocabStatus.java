package com.tma.english.models.dto.vocab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVocabStatus {
    private Instant lastTime;

    private int reviewInHour;

    private boolean isDifficult;

    private boolean ignored;
}

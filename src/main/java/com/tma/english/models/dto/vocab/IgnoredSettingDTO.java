package com.tma.english.models.dto.vocab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IgnoredSettingDTO {
    private Long wordId;
    private boolean ignored;
}

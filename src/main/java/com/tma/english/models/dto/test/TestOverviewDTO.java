package com.tma.english.models.dto.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class TestOverviewDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "ETS 2023 Test 1")
    private String testName;
    @Schema(example = "Ets 2023 Test 1")
    private String description;
    @Schema(example = "2023-06-30T06:58:32Z")
    private Instant startTime;
    @Schema(example = "120")
    private int timerInMinutes;
    @Schema(example = "100")
    private int numOfParticipants;
}

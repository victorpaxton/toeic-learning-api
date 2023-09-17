package com.tma.english.models.dto.test;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class TestCreateDTO {
    @Column(name = "test_name")
    @Schema(example = "ETS 2023 Test 1")
    private String testName;

    @Schema(example = "Ets 2023 Test 1")
    private String description;

    @Column(name = "start_time")
    @Schema(example = "2023-06-30T06:58:32Z")
    private Instant startTime;

    @Column(name = "timer_in_minutes")
    @Schema(example = "120")
    private int timerInMinutes;
}

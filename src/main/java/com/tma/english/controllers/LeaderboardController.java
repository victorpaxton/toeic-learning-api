package com.tma.english.controllers;

import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.services.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping
@Tag(name = "Leaderboard", description = "Get leaderboard of vocabulary or test module")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/leaderboard")
    @Operation(summary = "Get leaderboard")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel getLeaderboard(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam @Pattern(regexp = "^(TEST|VOCABULARY)$", message = "Session type must be 'TEST' or 'VOCABULARY'") String sessionType,
            @RequestParam @Pattern(regexp = "^(week|month|all)$", message = "Period must be 'week', 'month', or 'all'") String period) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(leaderboardService.getLeaderboard(pageNumber, pageSize, sessionType, period))
                .build();
    }

    @GetMapping("/leaderboard/my-rank")
    @Operation(summary = "Get your rank")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel getYourRank(
            Principal principal,
            @RequestParam @Pattern(regexp = "^(TEST|VOCABULARY)$", message = "Session type must be 'TEST' or 'VOCABULARY'") String sessionType,
            @RequestParam @Pattern(regexp = "^(week|month|all)$", message = "Period must be 'week', 'month', or 'all'") String period) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(leaderboardService.getYourRank(principal, sessionType, period))
                .build();
    }


}

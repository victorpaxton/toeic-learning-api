package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.attemptTest.request.SubmitTestDTO;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.services.test.AttemptTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping
@Tag(name = "Attempt Test Result", description = "Submit and attempt result management")
public class AttemptTestController {

    @Autowired
    private AttemptTestService attemptTestService;

    @PostMapping("/test/{id}/submit")
    @Operation(summary = "Submit test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel submitTest(Principal principal, @PathVariable Long id, @RequestBody @Valid SubmitTestDTO submitTestDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.submitTest(principal, id, submitTestDTO))
                .build();

    }

    @GetMapping("attempt-result/{id}")
    @Operation(summary = "Get overview result of a test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultOverview(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultOverview(id))
                .build();
    }

    @GetMapping("attempt-result/{id}/part1")
    @Operation(summary = "Get detailed results of part 1")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultPart1(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultPart1(id))
                .build();
    }

    @GetMapping("attempt-result/{id}/part2")
    @Operation(summary = "Get detailed results of part 2")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultPart2(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultPart2(id))
                .build();
    }

    @GetMapping("attempt-result/{id}/part3")
    @Operation(summary = "Get detailed results of part 3")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultPart3(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultPart3(id))
                .build();
    }

    @GetMapping("attempt-result/{id}/part4")
    @Operation(summary = "Get detailed results of part 4")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultPart4(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultPart4(id))
                .build();
    }

    @GetMapping("attempt-result/{id}/part5")
    @Operation(summary = "Get detailed results of part 5")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultPart5(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultPart5(id))
                .build();
    }

    @GetMapping("attempt-result/{id}/part6")
    @Operation(summary = "Get detailed results of part 6")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultPart6(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultPart6(id))
                .build();
    }

    @GetMapping("attempt-result/{id}/part7")
    @Operation(summary = "Get detailed results of part 7")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel attemptResultPart7(@PathVariable Long id) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(attemptTestService.getAttemptResultPart7(id))
                .build();
    }

}

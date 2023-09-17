package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.part1.QuestionP1CreateDTO;
import com.tma.english.services.test.QuestionPartOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/questions/part-one")
@Tag(name = "Questions Part 1", description = "Questions part 1 management")
public class QuestionP1Controller {

    @Autowired
    private QuestionPartOneService questionPartOneService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all questions of part 1")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getAllP1Questions() throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartOneService.getAll())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add a new question for part 1")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel addQuestionP1(@RequestBody QuestionP1CreateDTO questionP1CreateDTO) {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartOneService.addQuestion(questionP1CreateDTO))
                .build();
    }

    @PatchMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Assign question to test")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel assignQuestionP1ToTest(@PathVariable Long questionId, @RequestParam("test-id") Long testId)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartOneService.assignQuestionToTest(testId, questionId))
                .build();
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteP1Question(@PathVariable Long questionId)
            throws NotFoundException {
        questionPartOneService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel updateP1Question(@PathVariable Long questionId, @RequestBody @Valid QuestionP1CreateDTO questionP1CreateDTO)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartOneService.updateQuestion(questionId, questionP1CreateDTO))
                .build();
    }

    @GetMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getQuestion(@PathVariable Long questionId)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartOneService.getQuestion(questionId))
                .build();
    }

    @PostMapping(value = "/upload-file/{questionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload audio and image for a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel uploadFile(
            @PathVariable Long questionId,
            @RequestPart MultipartFile audio,
            @RequestPart MultipartFile image) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartOneService.uploadFile(questionId, audio, image))
                .build();
    }
}

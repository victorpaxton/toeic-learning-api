package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.part2.QuestionP2CreateDTO;
import com.tma.english.services.test.QuestionPartTwoService;
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
@RequestMapping("/questions/part-two")
@Tag(name = "Questions Part 2", description = "Questions part 2 management")
public class QuestionP2Controller {

    @Autowired
    private QuestionPartTwoService questionPartTwoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all questions of part 2")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getAllQuestions() throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartTwoService.getAll())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add a new question for part 2")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel addQuestion(@RequestBody @Valid QuestionP2CreateDTO questionP2CreateDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartTwoService.addQuestion(questionP2CreateDTO))
                .build();
    }

    @PatchMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Assign question to test")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel assignQuestionToTest(@PathVariable Long questionId, @RequestParam("test-id") Long testId)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartTwoService.assignQuestionToTest(testId, questionId))
                .build();
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteQuestion(@PathVariable Long questionId)
            throws NotFoundException {
        questionPartTwoService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel updateQuestion(@PathVariable Long questionId, @RequestBody @Valid QuestionP2CreateDTO questionP2CreateDTO)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartTwoService.updateQuestion(questionId, questionP2CreateDTO))
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
                .response(questionPartTwoService.getQuestion(questionId))
                .build();
    }

    @PostMapping(value = "/upload-file/{questionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload audio and image for a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel uploadFile(
            @PathVariable Long questionId,
            @RequestPart MultipartFile audio) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartTwoService.uploadFile(questionId, audio))
                .build();
    }
}

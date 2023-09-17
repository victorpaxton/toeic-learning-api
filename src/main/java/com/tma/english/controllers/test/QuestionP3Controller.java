package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.part3.GroupQuestionP3CreateDTO;
import com.tma.english.services.test.QuestionPartThreeService;
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
@RequestMapping("/questions/part-three")
@Tag(name = "Questions Part 3", description = "Questions part 3 management")
public class QuestionP3Controller {

    @Autowired
    QuestionPartThreeService questionPartThreeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all questions of part 3")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getAllQuestions() throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartThreeService.getAll())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add a new question for part 3")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel addQuestion(@RequestBody @Valid GroupQuestionP3CreateDTO groupQuestionP3CreateDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartThreeService.addQuestion(groupQuestionP3CreateDTO))
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
                .response(questionPartThreeService.getQuestion(questionId))
                .build();
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteQuestion(@PathVariable Long questionId)
            throws NotFoundException {
        questionPartThreeService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel updateQuestion(@PathVariable Long questionId, @RequestBody @Valid GroupQuestionP3CreateDTO groupQuestionP3CreateDTO)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartThreeService.updateQuestion(questionId, groupQuestionP3CreateDTO))
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
                .response(questionPartThreeService.assignQuestionToTest(testId, questionId))
                .build();
    }

    @PostMapping(value = "/upload-file/{questionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload audio and image for a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel uploadFile(
            @PathVariable Long questionId,
            @RequestPart MultipartFile audio,
            @RequestPart(required = false) MultipartFile image) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartThreeService.uploadFile(questionId, audio, image))
                .build();
    }

}

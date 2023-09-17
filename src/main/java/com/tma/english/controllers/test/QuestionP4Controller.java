package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.part4.GroupQuestionP4CreateDTO;
import com.tma.english.services.test.QuestionPartFourService;
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
@RequestMapping("/questions/part-four")
@Tag(name = "Questions Part 4", description = "Questions part 4 management")
public class QuestionP4Controller {

    @Autowired
    QuestionPartFourService questionPartFourService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all questions of part 4")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getAllQuestions() throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFourService.getAll())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add a new question for part 4")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel addQuestion(@RequestBody @Valid GroupQuestionP4CreateDTO groupQuestionP4CreateDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFourService.addQuestion(groupQuestionP4CreateDTO))
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
                .response(questionPartFourService.getQuestion(questionId))
                .build();
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteQuestion(@PathVariable Long questionId)
            throws NotFoundException {
        questionPartFourService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel updateQuestion(@PathVariable Long questionId, @RequestBody @Valid GroupQuestionP4CreateDTO groupQuestionP4CreateDTO)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFourService.updateQuestion(questionId, groupQuestionP4CreateDTO))
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
                .response(questionPartFourService.assignQuestionToTest(testId, questionId))
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
                .response(questionPartFourService.uploadFile(questionId, audio, image))
                .build();
    }

}

package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.part6.GroupQuestionP6CreateDTO;
import com.tma.english.services.test.QuestionPartSixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions/part-six")
@Tag(name = "Questions Part 6", description = "Questions part 6 management")
public class QuestionP6Controller {
    @Autowired
    QuestionPartSixService questionPartSixService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all questions of part 6")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getAllQuestions() throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSixService.getAll())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add a new question for part 6")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel addQuestion(@RequestBody @Valid GroupQuestionP6CreateDTO groupQuestionP6CreateDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSixService.addQuestion(groupQuestionP6CreateDTO))
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
                .response(questionPartSixService.getQuestion(questionId))
                .build();
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteQuestion(@PathVariable Long questionId)
            throws NotFoundException {
        questionPartSixService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel updateQuestion(@PathVariable Long questionId, @RequestBody @Valid GroupQuestionP6CreateDTO groupQuestionP6CreateDTO)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSixService.updateQuestion(questionId, groupQuestionP6CreateDTO))
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
                .response(questionPartSixService.assignQuestionToTest(testId, questionId))
                .build();
    }

}

package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.part6.GroupQuestionP6CreateDTO;
import com.tma.english.models.dto.test.part7.GroupQuestionP7CreateDTO;
import com.tma.english.services.test.QuestionPartSevenService;
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
@RequestMapping("/questions/part-seven")
@Tag(name = "Questions Part 7", description = "Questions part 7 management")
public class QuestionP7Controller {
    @Autowired
    QuestionPartSevenService questionPartSevenService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all questions of part 7")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getAllQuestions() throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSevenService.getAll())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add a new question for part 7")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel addQuestion(@RequestBody @Valid GroupQuestionP7CreateDTO groupQuestionP7CreateDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSevenService.addQuestion(groupQuestionP7CreateDTO))
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
                .response(questionPartSevenService.getQuestion(questionId))
                .build();
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteQuestion(@PathVariable Long questionId)
            throws NotFoundException {
        questionPartSevenService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel updateQuestion(@PathVariable Long questionId, @RequestBody @Valid GroupQuestionP7CreateDTO groupQuestionP7CreateDTO)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSevenService.updateQuestion(questionId, groupQuestionP7CreateDTO))
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
                .response(questionPartSevenService.assignQuestionToTest(testId, questionId))
                .build();
    }
}

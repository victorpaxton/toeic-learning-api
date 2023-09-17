package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.part1.QuestionP1CreateDTO;
import com.tma.english.models.dto.test.part5.QuestionP5CreateDTO;
import com.tma.english.services.test.QuestionPartFiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions/part-five")
@Tag(name = "Questions Part 5", description = "Questions part 5 management")
public class QuestionP5Controller {

    @Autowired
    private QuestionPartFiveService questionPartFiveService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all questions of part 1")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel getAllP1Questions() throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFiveService.getAll())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add a new question for part 1")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel addQuestionP1(@RequestBody @Valid QuestionP5CreateDTO questionP5CreateDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFiveService.addQuestion(questionP5CreateDTO))
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
                .response(questionPartFiveService.assignQuestionToTest(testId, questionId))
                .build();
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteP1Question(@PathVariable Long questionId)
            throws NotFoundException {
        questionPartFiveService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a question")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel updateP1Question(@PathVariable Long questionId, @RequestBody @Valid QuestionP5CreateDTO questionP5CreateDTO)
            throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFiveService.updateQuestion(questionId, questionP5CreateDTO))
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
                .response(questionPartFiveService.getQuestion(questionId))
                .build();
    }
}

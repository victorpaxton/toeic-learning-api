package com.tma.english.controllers.vocab;

import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.vocab.IgnoredSettingDTO;
import com.tma.english.models.dto.vocab.vocab_template.CompleteLessonRequestDTO;
import com.tma.english.services.vocab.UserVocabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/vocab-learning")
@Tag(name = "User vocabulary learning", description = "User vocabulary learning results")
public class UserVocabController {
    @Autowired
    private UserVocabService userVocabService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Get all topics")
    public ResponseModel getAllLessons(Principal principal,
                                       @RequestParam(defaultValue = "") String keyword,
                                       @RequestParam(defaultValue = "0") int pageNumber,
                                       @RequestParam(defaultValue = "4") int pageSize,
                                       @RequestParam(defaultValue = "id") String sortField) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userVocabService.getAllLessons(principal, keyword, pageNumber, pageSize, sortField))
                .build();
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a detailed topic")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel getLesson(Principal principal,
                                   @PathVariable Long categoryId,
                                   @RequestParam(defaultValue = "0") int pageNumber,
                                   @RequestParam(defaultValue = "5") int pageSize,
                                   @RequestParam(defaultValue = "english") String sortField) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userVocabService.getLesson(principal, categoryId, pageNumber, pageSize, sortField))
                .build();
    }

    @GetMapping("/{categoryId}/lesson")
    @Operation(summary = "Continue learning a topic")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel getLearningTemplate(Principal principal,
                                             @PathVariable Long categoryId,
                                             @RequestParam Long limit) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userVocabService.continueLearning(principal, categoryId, limit))
                .build();
    }


    @PostMapping("/{categoryId}/lesson")
    @Operation(summary = "Complete a lesson of a topic")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel completeLesson(Principal principal,
                                        @PathVariable Long categoryId,
                                        @RequestBody @Valid CompleteLessonRequestDTO completeLessonRequestDTO) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userVocabService.completeLesson(principal, categoryId, completeLessonRequestDTO))
                .build();
    }


    @GetMapping("/{categoryId}/review")
    @Operation(summary = "Review a topic")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel reviewLesson(Principal principal,
                                      @PathVariable Long categoryId,
                                      @RequestParam Long limit) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userVocabService.getReviewLesson(principal, categoryId, limit))
                .build();
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Ignored words setting")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity ignoredSetting(Principal principal,
                                         @PathVariable Long categoryId,
                                         @RequestBody List<IgnoredSettingDTO> ignoredRequest) {
        userVocabService.ignoredSetting(principal, categoryId, ignoredRequest);
        return ResponseEntity.noContent().build();
    }

}

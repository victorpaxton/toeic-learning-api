package com.tma.english.controllers.vocab;

import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.vocab.AddNewWordDTO;
import com.tma.english.models.dto.vocab.VocabCategoryCreateDTO;
import com.tma.english.services.vocab.VocabService;
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
@RequestMapping("/vocab-categories")
@Tag(name = "Vocabulary category", description = "Vocabulary category management")
public class VocabCategoryController {

    @Autowired
    private VocabService vocabService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all topics")
    public ResponseModel getAllCategories() {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.getAllCategories())
                .build();
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a detailed topic")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel getLesson(@PathVariable Long categoryId) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.getLesson(categoryId))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Add new topic")
    public ResponseModel createNewVocabCategory(@RequestBody @Valid VocabCategoryCreateDTO vocabCategoryCreateDTO) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.createNewVocabCategory(vocabCategoryCreateDTO))
                .build();
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update topic name")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel updateTopic(@PathVariable Long categoryId, @RequestBody @Valid VocabCategoryCreateDTO vocabCategoryCreateDTO) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.updateTopic(categoryId, vocabCategoryCreateDTO))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete a topic")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteTopic(@PathVariable Long categoryId) {
        vocabService.deleteTopic(categoryId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{categoryId}")
    @Operation(summary = "Add new word for a topic")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseModel addNewWord(@PathVariable Long categoryId, @RequestBody @Valid AddNewWordDTO addNewWordDTO) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.addNewWord(categoryId, addNewWordDTO))
                .build();
    }

    @PostMapping(value = "/{categoryId}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload image for category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel uploadCategoryImage(@PathVariable Long categoryId,
                                             @RequestPart MultipartFile image) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.uploadCategoryImage(categoryId, image))
                .build();
    }
}

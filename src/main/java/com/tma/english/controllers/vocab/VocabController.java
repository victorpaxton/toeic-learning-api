package com.tma.english.controllers.vocab;

import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.vocab.vocab_template.VocabTemplateCreateDTO;
import com.tma.english.services.vocab.VocabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vocabulary")
@Tag(name = "Vocabulary", description = "Vocabulary management")
public class VocabController {
    @Autowired
    private VocabService vocabService;

    @PostMapping("/{wordId}/template")
    @Operation(summary = "Create the template for word by id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel createTemplate(@PathVariable Long wordId, @RequestBody @Valid VocabTemplateCreateDTO vocabTemplateCreateDTO) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.createTemplate(wordId, vocabTemplateCreateDTO))
                .build();
    }

    @PostMapping(value = "/{wordId}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload image for a word")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel uploadWordImage(@PathVariable Long wordId,
                                             @RequestPart MultipartFile image) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.uploadWordImage(wordId, image))
                .build();
    }

    @PostMapping(value = "/{wordId}/upload-audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload audio for a word")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel uploadWordAudio(@PathVariable Long wordId,
                                         @RequestPart MultipartFile audio) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.uploadWordAudio(wordId, audio))
                .build();
    }

    @PostMapping(value = "/{wordId}/upload-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload video for a word")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseModel uploadWordVideo(@PathVariable Long wordId,
                                         @RequestPart MultipartFile video) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(vocabService.uploadWordVideo(wordId, video))
                .build();
    }


}

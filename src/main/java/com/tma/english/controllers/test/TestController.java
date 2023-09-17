package com.tma.english.controllers.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.test.TestCreateDTO;
import com.tma.english.models.dto.test.TestOverviewDTO;
import com.tma.english.models.dto.test.part1.QuestionP1CreateDTO;
import com.tma.english.models.dto.test.part2.QuestionP2CreateDTO;
import com.tma.english.models.dto.test.part3.GroupQuestionP3CreateDTO;
import com.tma.english.models.dto.test.part4.GroupQuestionP4CreateDTO;
import com.tma.english.models.dto.test.part5.QuestionP5CreateDTO;
import com.tma.english.models.dto.test.part6.GroupQuestionP6CreateDTO;
import com.tma.english.models.dto.test.part7.GroupQuestionP7CreateDTO;
import com.tma.english.services.test.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tests")
@Tag(name = "Test", description = "Test management")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private QuestionPartOneService questionPartOneService;

    @Autowired
    private QuestionPartTwoService questionPartTwoService;

    @Autowired
    private QuestionPartThreeService questionPartThreeService;

    @Autowired
    private QuestionPartFourService questionPartFourService;

    @Autowired
    private QuestionPartFiveService questionPartFiveService;

    @Autowired
    private QuestionPartSixService questionPartSixService;

    @Autowired
    private QuestionPartSevenService questionPartSevenService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of all tests")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getAllTests(@RequestParam(defaultValue = "") String keyword,
                                     @RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "4") int pageSize) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getAllTests(keyword, pageNumber, pageSize))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel createTest(@RequestBody @Valid TestCreateDTO testCreateDTO) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.createTest(testCreateDTO))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get overview of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getTest(@PathVariable Long id) throws NotFoundException {
        TestOverviewDTO test = testService.getTest(id);
        return ResponseModel.builder()
                .isSuccess(true)
                .response(test)
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update overview of a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel updateTestOverview(@PathVariable Long id, @RequestBody @Valid TestCreateDTO testCreateDTO) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.updateTestOverview(id, testCreateDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteTest(@PathVariable Long id) throws NotFoundException {
        testService.deleteTestById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/part-one")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of part 1 questions of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getP1QuestionsByTest(@PathVariable Long id) throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getP1Questions(id))
                .build();
    }

    @GetMapping("/{id}/part-two")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of part 2 questions of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getP2Questions(@PathVariable Long id) throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getP2Questions(id))
                .build();
    }

    @GetMapping("/{id}/part-three")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of part 3 questions of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getP3Questions(@PathVariable Long id) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getP3Questions(id))
                .build();
    }

    @GetMapping("/{id}/part-four")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of part 4 questions of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getP4Questions(@PathVariable Long id) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getP4Questions(id))
                .build();
    }

    @GetMapping("/{id}/part-five")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of part 5 questions of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getP5Questions(@PathVariable Long id) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getP5Questions(id))
                .build();
    }

    @GetMapping("/{id}/part-six")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of part 6 questions of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getP6Questions(@PathVariable Long id) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getP6Questions(id))
                .build();
    }

    @GetMapping("/{id}/part-seven")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of part 7 questions of a test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseModel getP7Questions(@PathVariable Long id) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(testService.getP7Questions(id))
                .build();
    }

    @PostMapping("/{id}/part-one")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question for part 1 for a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel addQuestionP1ByTest(@PathVariable Long id, @RequestBody @Valid QuestionP1CreateDTO questionP1CreateDTO)
            throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartOneService.addQuestionByTest(id, questionP1CreateDTO))
                .build();
    }

    @PostMapping("/{id}/part-two")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question for part 2 for a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel addQuestionP2ByTest(@PathVariable Long id, @RequestBody @Valid QuestionP2CreateDTO questionP2CreateDTO)
            throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartTwoService.addQuestionByTest(id, questionP2CreateDTO))
                .build();
    }

    @PostMapping("/{id}/part-three")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question for part 3 for a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel addQuestionP3ByTest(@PathVariable Long id, @RequestBody @Valid GroupQuestionP3CreateDTO groupQuestionP3CreateDTO)
            throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartThreeService.addQuestionByTest(id, groupQuestionP3CreateDTO))
                .build();
    }

    @PostMapping("/{id}/part-four")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question for part 4 for a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel addQuestionP4ByTest(@PathVariable Long id, @RequestBody @Valid GroupQuestionP4CreateDTO groupQuestionP4CreateDTO)
            throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFourService.addQuestionByTest(id, groupQuestionP4CreateDTO))
                .build();
    }

    @PostMapping("/{id}/part-five")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question for part 5 for a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel addQuestionP5ByTest(@PathVariable Long id, @RequestBody @Valid QuestionP5CreateDTO questionP5CreateDTO)
            throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartFiveService.addQuestionByTest(id, questionP5CreateDTO))
                .build();
    }

    @PostMapping("/{id}/part-six")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question for part 6 for a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel addQuestionP6ByTest(@PathVariable Long id, @RequestBody @Valid GroupQuestionP6CreateDTO groupQuestionP6CreateDTO)
            throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSixService.addQuestionByTest(id, groupQuestionP6CreateDTO))
                .build();
    }

    @PostMapping("/{id}/part-seven")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question for part 7 for a test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel addQuestionP7ByTest(@PathVariable Long id, @RequestBody @Valid GroupQuestionP7CreateDTO groupQuestionP7CreateDTO)
            throws NotFoundException {

        return ResponseModel.builder()
                .isSuccess(true)
                .response(questionPartSevenService.addQuestionByTest(id, groupQuestionP7CreateDTO))
                .build();
    }



}

package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.mapper.QuestionPartSixMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.test.part6.GroupQuestionAndAnswerP6DTO;
import com.tma.english.models.dto.test.part6.GroupQuestionP6CreateDTO;
import com.tma.english.models.dto.test.part6.QuestionP6CreateDTO;
import com.tma.english.models.entities.test.GroupQuestionPartSix;
import com.tma.english.models.entities.test.QuestionPartSix;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.GroupQuestionPartSixRepository;
import com.tma.english.repositories.test.QuestionPartSixRepository;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionPartSixService {

    @Autowired
    private GroupQuestionPartSixRepository groupQuestionPartSixRepository;

    @Autowired
    private QuestionPartSixRepository questionPartSixRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private QuestionPartSixMapper questionPartSixMapper;

    public List<GroupQuestionAndAnswerP6DTO> getAll() {

        return groupQuestionPartSixRepository.findAll()
                .stream().map(q -> {
                    GroupQuestionAndAnswerP6DTO questionAndAnswerP6DTO = questionPartSixMapper.groupQuestionPartSixToGroupQuestionAndAnswerP6DTO(q);

                    questionAndAnswerP6DTO.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartSixMapper::questionPartSixToQuestionAndAnswerP6DTO)
                                    .collect(Collectors.toList())
                    );
                    questionAndAnswerP6DTO.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return questionAndAnswerP6DTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupQuestionAndAnswerP6DTO addQuestion(GroupQuestionP6CreateDTO groupQuestionP6CreateDTO) {

        if (groupQuestionP6CreateDTO.getQuestionsList().size() != 4) {
            throw new BadRequestException("There must be 4 single questions for a group");
        }

        // save to database
        GroupQuestionPartSix groupQuestion = groupQuestionPartSixRepository.save(
                GroupQuestionPartSix.builder()
                        .questionNo(groupQuestionP6CreateDTO.getQuestionNo())
                        .passageText(groupQuestionP6CreateDTO.getPassageText())
                        .passageType(groupQuestionP6CreateDTO.getPassageType())
                        .inVietnamese(groupQuestionP6CreateDTO.getInVietnamese())
                        .build()
        );

        List<QuestionPartSix> listQuestions = groupQuestionP6CreateDTO.getQuestionsList()
                .stream().map(questionPartSixMapper::questionP6CreateDTOToQuestionPartSix)
                .map(q -> {
                    q.setGroupQuestionPartSix(groupQuestion);
                    return questionPartSixRepository.save(q);
                }).toList();

        // construct response data

        GroupQuestionAndAnswerP6DTO response = questionPartSixMapper.groupQuestionPartSixToGroupQuestionAndAnswerP6DTO(
                groupQuestion
        );

        response.setQuestionList(
                listQuestions.stream().map(questionPartSixMapper::questionPartSixToQuestionAndAnswerP6DTO)
                        .toList()
        );

        return response;
    }

    public GroupQuestionAndAnswerP6DTO getQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartSix groupQuestion = groupQuestionPartSixRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        GroupQuestionAndAnswerP6DTO response =
                questionPartSixMapper.groupQuestionPartSixToGroupQuestionAndAnswerP6DTO(groupQuestion);

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartSixMapper::questionPartSixToQuestionAndAnswerP6DTO)
                        .toList()
        );

        response.setTestOverviewDTO(testMapper.testToTestOverviewDTO(groupQuestion.getTest()));

        return response;
    }

    public void deleteQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartSix groupQuestion = groupQuestionPartSixRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        groupQuestionPartSixRepository.delete(groupQuestion);
    }

    public GroupQuestionAndAnswerP6DTO updateQuestion(Long questionId, GroupQuestionP6CreateDTO groupQuestionP6CreateDTO)
            throws NotFoundException {
        GroupQuestionPartSix groupQuestion = groupQuestionPartSixRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

//        update data
        groupQuestion.setPassageText(groupQuestionP6CreateDTO.getPassageText());
        groupQuestion.setPassageType(groupQuestionP6CreateDTO.getPassageType());

        IntStream.range(0, groupQuestion.getQuestionsList().size())
                .forEach(i -> {
                    QuestionPartSix question = groupQuestion.getQuestionsList().get(i);
                    QuestionP6CreateDTO updateQuestion = groupQuestionP6CreateDTO.getQuestionsList().get(i);

                    question.setOption1(updateQuestion.getOption1());
                    question.setOption2(updateQuestion.getOption2());
                    question.setOption3(updateQuestion.getOption3());
                    question.setOption4(updateQuestion.getOption4());
                    question.setCorrectAnswer(updateQuestion.getCorrectAnswer());
                    question.setExplanationAnswer(updateQuestion.getExplanationAnswer());
                    question.setDifficultyLevel(updateQuestion.getDifficultyLevel());
                });

        groupQuestionPartSixRepository.save(groupQuestion);

//        construct response data
        GroupQuestionAndAnswerP6DTO response = questionPartSixMapper.groupQuestionPartSixToGroupQuestionAndAnswerP6DTO(
                groupQuestion
        );

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartSixMapper::questionPartSixToQuestionAndAnswerP6DTO)
                        .toList()
        );

        return response;
    }

    public Object assignQuestionToTest(Long testId, Long questionId) throws NotFoundException {
        GroupQuestionPartSix groupQuestion = groupQuestionPartSixRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        if (groupQuestion.getTest() != null) {
            throw new BadRequestException("This question is belonging to " + groupQuestion.getTest().getTestName());
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        groupQuestion.setTest(test);

        groupQuestionPartSixRepository.save(groupQuestion);

        return null;
    }

    @Transactional
    public GroupQuestionAndAnswerP6DTO addQuestionByTest(Long testId, GroupQuestionP6CreateDTO groupQuestionP6CreateDTO) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Not found test with id: " + testId));

        if (groupQuestionP6CreateDTO.getQuestionsList().size() != 4) {
            throw new BadRequestException("There must be 4 single questions for a group");
        }

        // save to database
        GroupQuestionPartSix groupQuestion = groupQuestionPartSixRepository.save(
                GroupQuestionPartSix.builder()
                        .questionNo(groupQuestionP6CreateDTO.getQuestionNo())
                        .passageText(groupQuestionP6CreateDTO.getPassageText())
                        .passageType(groupQuestionP6CreateDTO.getPassageType())
                        .inVietnamese(groupQuestionP6CreateDTO.getInVietnamese())
                        .build()
        );

        groupQuestion.setTest(test);

        List<QuestionPartSix> listQuestions =  groupQuestionP6CreateDTO.getQuestionsList()
                .stream().map(questionPartSixMapper::questionP6CreateDTOToQuestionPartSix)
                .map(q -> {
                    q.setGroupQuestionPartSix(groupQuestion);
                    return questionPartSixRepository.save(q);
                }).toList();

        // construct response data

        GroupQuestionAndAnswerP6DTO response = questionPartSixMapper.groupQuestionPartSixToGroupQuestionAndAnswerP6DTO(
                groupQuestion
        );

        response.setQuestionList(
                listQuestions.stream().map(questionPartSixMapper::questionPartSixToQuestionAndAnswerP6DTO)
                        .toList()
        );

        return response;

    }
}

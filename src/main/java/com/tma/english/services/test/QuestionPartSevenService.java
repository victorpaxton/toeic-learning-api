package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.mapper.QuestionPartSevenMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.test.part7.GroupQuestionAndAnswerP7DTO;
import com.tma.english.models.dto.test.part7.GroupQuestionP7CreateDTO;
import com.tma.english.models.dto.test.part7.QuestionP7CreateDTO;
import com.tma.english.models.entities.test.GroupQuestionPartSeven;
import com.tma.english.models.entities.test.QuestionPartSeven;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.GroupQuestionPartSevenRepository;
import com.tma.english.repositories.test.QuestionPartSevenRepository;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionPartSevenService {

    @Autowired
    private GroupQuestionPartSevenRepository groupQuestionPartSevenRepository;

    @Autowired
    private QuestionPartSevenRepository questionPartSevenRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private QuestionPartSevenMapper questionPartSevenMapper;

    public List<GroupQuestionAndAnswerP7DTO> getAll() {

        return groupQuestionPartSevenRepository.findAll()
                .stream().map(q -> {
                    GroupQuestionAndAnswerP7DTO questionAndAnswerP6DTO = questionPartSevenMapper.groupQuestionPartSevenToGroupQuestionAndAnswerP7DTO(q);

                    questionAndAnswerP6DTO.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartSevenMapper::questionPartSevenToQuestionAndAnswerP7DTO)
                                    .collect(Collectors.toList())
                    );
                    questionAndAnswerP6DTO.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return questionAndAnswerP6DTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupQuestionAndAnswerP7DTO addQuestion(GroupQuestionP7CreateDTO groupQuestionP7CreateDTO) {

        // save to database
        GroupQuestionPartSeven groupQuestion = groupQuestionPartSevenRepository.save(
                GroupQuestionPartSeven.builder()
                        .questionNo(groupQuestionP7CreateDTO.getQuestionNo())
                        .numOfPassages(groupQuestionP7CreateDTO.getNumOfPassages())
                        .passageText(groupQuestionP7CreateDTO.getPassageText())
                        .passageType(groupQuestionP7CreateDTO.getPassageType())
                        .image(groupQuestionP7CreateDTO.getImage())
                        .inVietnamese(groupQuestionP7CreateDTO.getInVietnamese())
                        .build()
        );

        List<QuestionPartSeven> listQuestions = groupQuestionP7CreateDTO.getQuestionsList()
                .stream().map(questionPartSevenMapper::questionP7CreateDTOToQuestionPartSeven)
                .map(q -> {
                    q.setGroupQuestionPartSeven(groupQuestion);
                    return questionPartSevenRepository.save(q);
                }).toList();

        // construct response data

        GroupQuestionAndAnswerP7DTO response = questionPartSevenMapper.groupQuestionPartSevenToGroupQuestionAndAnswerP7DTO(
                groupQuestion
        );

        response.setQuestionList(
                listQuestions.stream().map(questionPartSevenMapper::questionPartSevenToQuestionAndAnswerP7DTO)
                        .toList()
        );

        return response;
    }

    public GroupQuestionAndAnswerP7DTO getQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartSeven groupQuestion = groupQuestionPartSevenRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        GroupQuestionAndAnswerP7DTO response =
                questionPartSevenMapper.groupQuestionPartSevenToGroupQuestionAndAnswerP7DTO(groupQuestion);

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartSevenMapper::questionPartSevenToQuestionAndAnswerP7DTO)
                        .toList()
        );

        response.setTestOverviewDTO(testMapper.testToTestOverviewDTO(groupQuestion.getTest()));

        return response;
    }

    public void deleteQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartSeven groupQuestion = groupQuestionPartSevenRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        groupQuestionPartSevenRepository.delete(groupQuestion);
    }

    public GroupQuestionAndAnswerP7DTO updateQuestion(Long questionId, GroupQuestionP7CreateDTO groupQuestionP7CreateDTO)
            throws NotFoundException {
        GroupQuestionPartSeven groupQuestion = groupQuestionPartSevenRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

//        update data
        groupQuestion.setNumOfPassages(groupQuestionP7CreateDTO.getNumOfPassages());
        groupQuestion.setPassageText(groupQuestionP7CreateDTO.getPassageText());
        groupQuestion.setPassageType(groupQuestionP7CreateDTO.getPassageType());
        groupQuestion.setImage(groupQuestionP7CreateDTO.getImage());

        IntStream.range(0, groupQuestion.getQuestionsList().size())
                .forEach(i -> {
                    QuestionPartSeven question = groupQuestion.getQuestionsList().get(i);
                    QuestionP7CreateDTO updateQuestion = groupQuestionP7CreateDTO.getQuestionsList().get(i);

                    question.setQuestionText(updateQuestion.getQuestionText());
                    question.setOption1(updateQuestion.getOption1());
                    question.setOption2(updateQuestion.getOption2());
                    question.setOption3(updateQuestion.getOption3());
                    question.setOption4(updateQuestion.getOption4());
                    question.setCorrectAnswer(updateQuestion.getCorrectAnswer());
                    question.setExplanationAnswer(updateQuestion.getExplanationAnswer());
                    question.setDifficultyLevel(updateQuestion.getDifficultyLevel());
                });

        groupQuestionPartSevenRepository.save(groupQuestion);

//        construct response data
        GroupQuestionAndAnswerP7DTO response = questionPartSevenMapper.groupQuestionPartSevenToGroupQuestionAndAnswerP7DTO(
                groupQuestion
        );

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartSevenMapper::questionPartSevenToQuestionAndAnswerP7DTO)
                        .toList()
        );

        return response;
    }

    public Object assignQuestionToTest(Long testId, Long questionId) throws NotFoundException {
        GroupQuestionPartSeven groupQuestion = groupQuestionPartSevenRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        if (groupQuestion.getTest() != null) {
            throw new BadRequestException("This question is belonging to " + groupQuestion.getTest().getTestName());
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        groupQuestion.setTest(test);

        groupQuestionPartSevenRepository.save(groupQuestion);

        return null;
    }

    public GroupQuestionAndAnswerP7DTO addQuestionByTest(Long testId, GroupQuestionP7CreateDTO groupQuestionP7CreateDTO) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Not found test with id: " + testId));

        // save to database
        GroupQuestionPartSeven groupQuestion = groupQuestionPartSevenRepository.save(
                GroupQuestionPartSeven.builder()
                        .questionNo(groupQuestionP7CreateDTO.getQuestionNo())
                        .numOfPassages(groupQuestionP7CreateDTO.getNumOfPassages())
                        .passageText(groupQuestionP7CreateDTO.getPassageText())
                        .passageType(groupQuestionP7CreateDTO.getPassageType())
                        .image(groupQuestionP7CreateDTO.getImage())
                        .inVietnamese(groupQuestionP7CreateDTO.getInVietnamese())
                        .build()
        );

        groupQuestion.setTest(test);

        List<QuestionPartSeven> listQuestions = groupQuestionP7CreateDTO.getQuestionsList()
                .stream().map(questionPartSevenMapper::questionP7CreateDTOToQuestionPartSeven)
                .map(q -> {
                    q.setGroupQuestionPartSeven(groupQuestion);
                    return questionPartSevenRepository.save(q);
                }).toList();

        // construct response data

        GroupQuestionAndAnswerP7DTO response = questionPartSevenMapper.groupQuestionPartSevenToGroupQuestionAndAnswerP7DTO(
                groupQuestion
        );

        response.setQuestionList(
                listQuestions.stream().map(questionPartSevenMapper::questionPartSevenToQuestionAndAnswerP7DTO)
                        .toList()
        );

        return response;
    }
}

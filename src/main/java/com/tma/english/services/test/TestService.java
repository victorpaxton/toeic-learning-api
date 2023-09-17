package com.tma.english.services.test;

import com.tma.english.exception.NotFoundException;
import com.tma.english.mapper.*;
import com.tma.english.models.dto.test.TestCreateDTO;
import com.tma.english.models.dto.test.TestOverviewDTO;
import com.tma.english.models.dto.test.part1.QuestionP1ResponseDTO;
import com.tma.english.models.dto.test.part2.QuestionP2ResponseDTO;
import com.tma.english.models.dto.test.part3.GroupQuestionP3DTO;
import com.tma.english.models.dto.test.part3.QuestionP3DTO;
import com.tma.english.models.dto.test.part4.GroupQuestionP4DTO;
import com.tma.english.models.dto.test.part4.QuestionP4DTO;
import com.tma.english.models.dto.test.part5.QuestionP5ResponseDTO;
import com.tma.english.models.dto.test.part6.GroupQuestionP6DTO;
import com.tma.english.models.dto.test.part6.QuestionP6DTO;
import com.tma.english.models.dto.test.part7.GroupQuestionP7DTO;
import com.tma.english.models.dto.test.part7.QuestionP7DTO;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private QuestionPartOneMapper questionPartOneMapper;

    @Autowired
    private QuestionPartTwoMapper questionPartTwoMapper;

    @Autowired
    private QuestionPartThreeMapper questionPartThreeMapper;

    @Autowired
    private QuestionPartFourMapper questionPartFourMapper;

    @Autowired
    private QuestionPartFiveMapper questionPartFiveMapper;

    @Autowired
    private QuestionPartSixMapper questionPartSixMapper;

    @Autowired
    private QuestionPartSevenMapper questionPartSevenMapper;

    public Page<TestOverviewDTO> getAllTests(String keyword, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return testRepository.findByCriterion(keyword, pageable)
                .map(testMapper::testToTestOverviewDTO);
    }

    public TestOverviewDTO getTest(Long testId) throws NotFoundException{

        return testRepository.findById(testId)
                .map(testMapper::testToTestOverviewDTO)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));
    }

    public TestOverviewDTO createTest(TestCreateDTO testCreateDTO) {
        Test newTest = testMapper.testCreateDtoToTest(testCreateDTO);
        newTest.setNumOfParticipants(0);

        return testMapper.testToTestOverviewDTO(testRepository.save(newTest));
    }

    public TestOverviewDTO updateTestOverview(Long testId, TestCreateDTO testCreateDTO) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        test.setTestName(testCreateDTO.getTestName());
        test.setDescription(testCreateDTO.getDescription());
        test.setStartTime(testCreateDTO.getStartTime());
        test.setTimerInMinutes(testCreateDTO.getTimerInMinutes());

        testRepository.save(test);

        return testMapper.testToTestOverviewDTO(test);
    }

    public List<QuestionP1ResponseDTO> getP1Questions(Long testId) throws NotFoundException{
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        return test.getQuestionPartOnes()
                .stream().map(questionPartOneMapper::questionPartOneToQuestionP1ResponseDTO)
                .sorted(Comparator.comparing(QuestionP1ResponseDTO::getQuestionNo))
                .collect(Collectors.toList());
    }

    public List<QuestionP2ResponseDTO> getP2Questions(Long testId) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        return test.getQuestionPartTwos()
                .stream().map(questionPartTwoMapper::questionPartTwoToQuestionP2ResponseDTO)
                .sorted(Comparator.comparing(QuestionP2ResponseDTO::getQuestionNo))
                .collect(Collectors.toList());
    }

    public void deleteTestById(Long testId) throws NotFoundException{
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));
        testRepository.delete(test);
    }

    public List<GroupQuestionP3DTO> getP3Questions(Long testId) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        return test.getGroupQuestionPartThrees()
                .stream().map(q -> {
                    GroupQuestionP3DTO groupQuestion = questionPartThreeMapper.groupQuestionPartThreeToGroupQuestionP3DTO(q);

                    groupQuestion.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartThreeMapper::questionPartThreeToQuestionP3DTO)
                                    .sorted(Comparator.comparing(QuestionP3DTO::getQuestionNo))
                                    .collect(Collectors.toList())
                    );
//                    groupQuestion.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return groupQuestion;
                })
                .sorted(Comparator.comparing(GroupQuestionP3DTO::getQuestionNo))
                .collect(Collectors.toList());
    }

    public List<GroupQuestionP4DTO> getP4Questions(Long testId) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        return test.getGroupQuestionPartFours()
                .stream().map(q -> {
                    GroupQuestionP4DTO groupQuestion = questionPartFourMapper.groupQuestionPartFourToGroupQuestionP4DTO(q);

                    groupQuestion.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartFourMapper::questionPartFourToQuestionP4DTO)
                                    .sorted(Comparator.comparing(QuestionP4DTO::getQuestionNo))
                                    .collect(Collectors.toList())
                    );
//                    groupQuestion.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return groupQuestion;
                })
                .sorted(Comparator.comparing(GroupQuestionP4DTO::getQuestionNo))
                .collect(Collectors.toList());
    }


    public List<QuestionP5ResponseDTO> getP5Questions(Long testId) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        return test.getQuestionPartFives()
                .stream().map(questionPartFiveMapper::questionPartFiveToQuestionP5ResponseDTO)
                .sorted(Comparator.comparing(QuestionP5ResponseDTO::getQuestionNo))
                .collect(Collectors.toList());
    }

    public List<GroupQuestionP6DTO> getP6Questions(Long testId) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        return test.getGroupQuestionPartSixes()
                .stream().map(q -> {
                    GroupQuestionP6DTO groupQuestion = questionPartSixMapper.groupQuestionPartSixToGroupQuestionP6DTO(q);

                    groupQuestion.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartSixMapper::questionPartSixToQuestionP6DTO)
                                    .sorted(Comparator.comparing(QuestionP6DTO::getQuestionNo))
                                    .collect(Collectors.toList())
                    );
//                    groupQuestion.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return groupQuestion;
                })
                .sorted(Comparator.comparing(GroupQuestionP6DTO::getQuestionNo))
                .collect(Collectors.toList());
    }

    public List<GroupQuestionP7DTO> getP7Questions(Long testId) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found with id " + testId));

        return test.getGroupQuestionPartSevens()
                .stream().map(q -> {
                    GroupQuestionP7DTO groupQuestion = questionPartSevenMapper.groupQuestionPartSevenToGroupQuestionP7DTO(q);

                    groupQuestion.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartSevenMapper::questionPartSevenToQuestionP7DTO)
                                    .sorted(Comparator.comparing(QuestionP7DTO::getQuestionNo))
                                    .collect(Collectors.toList())
                    );
//                    groupQuestion.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return groupQuestion;
                })
                .sorted(Comparator.comparing(GroupQuestionP7DTO::getQuestionNo))
                .collect(Collectors.toList());
    }
}

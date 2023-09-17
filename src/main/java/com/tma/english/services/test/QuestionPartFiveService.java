package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.mapper.QuestionPartFiveMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.test.part5.QuestionAndAnswerP5DTO;
import com.tma.english.models.dto.test.part5.QuestionP5CreateDTO;
import com.tma.english.models.entities.test.QuestionPartFive;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.QuestionPartFiveRepository;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionPartFiveService {
    @Autowired
    QuestionPartFiveRepository questionPartFiveRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    QuestionPartFiveMapper questionPartFiveMapper;

    @Autowired
    TestMapper testMapper;

    public QuestionAndAnswerP5DTO addQuestionByTest(Long testId, QuestionP5CreateDTO questionP5CreateDTO) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Not found test with id: " + testId));

        QuestionPartFive question = questionPartFiveMapper.questionP5CreateDTOToQuestionPartFive(questionP5CreateDTO);
        question.setTest(test);

        return questionPartFiveMapper.questionPartFiveToQuestionAndAnswerP5DTO(
                questionPartFiveRepository.save(question)
        );
    }

    public List<QuestionAndAnswerP5DTO> getAll() {
        return questionPartFiveRepository.findAll()
                .stream().map(questionPartFiveMapper::questionPartFiveToQuestionAndAnswerP5DTO)
                .collect(Collectors.toList());
    }

    public QuestionAndAnswerP5DTO addQuestion(QuestionP5CreateDTO questionP5CreateDTO) {
        QuestionPartFive question = questionPartFiveMapper.questionP5CreateDTOToQuestionPartFive(questionP5CreateDTO);

        return questionPartFiveMapper.questionPartFiveToQuestionAndAnswerP5DTO(
                questionPartFiveRepository.save(question)
        );
    }

    public List<QuestionAndAnswerP5DTO> assignQuestionToTest(Long testId, Long questionId) throws NotFoundException {
        QuestionPartFive question = questionPartFiveRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        if (question.getTest() != null) {
            throw new BadRequestException("This question is belonging to " + question.getTest().getTestName());
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        question.setTest(test);
        questionPartFiveRepository.save(question);

        return test.getQuestionPartFives()
                .stream().map(questionPartFiveMapper::questionPartFiveToQuestionAndAnswerP5DTO)
                .collect(Collectors.toList());
    }

    public void deleteQuestion(Long questionId) throws NotFoundException {
        QuestionPartFive question = questionPartFiveRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        questionPartFiveRepository.delete(question);
    }

    public QuestionAndAnswerP5DTO updateQuestion(Long questionId, QuestionP5CreateDTO questionP5CreateDTO)
            throws NotFoundException {
        QuestionPartFive question = questionPartFiveRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        question.setQuestionText(questionP5CreateDTO.getQuestionText());
        question.setOption1(questionP5CreateDTO.getOption1());
        question.setOption2(questionP5CreateDTO.getOption2());
        question.setOption3(questionP5CreateDTO.getOption3());
        question.setOption4(questionP5CreateDTO.getOption4());
        question.setCorrectAnswer(questionP5CreateDTO.getCorrectAnswer());
        question.setExplanationAnswer(questionP5CreateDTO.getExplanationAnswer());
        question.setDifficultyLevel(questionP5CreateDTO.getDifficultyLevel());

        questionPartFiveRepository.save(question);
        return questionPartFiveMapper.questionPartFiveToQuestionAndAnswerP5DTO(question);
    }

    public QuestionAndAnswerP5DTO getQuestion(Long questionId) throws NotFoundException {
        QuestionPartFive question = questionPartFiveRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        QuestionAndAnswerP5DTO ret = questionPartFiveMapper.questionPartFiveToQuestionAndAnswerP5DTO(question);
        ret.setTestOverviewDTO(testMapper.testToTestOverviewDTO(question.getTest()));

        return ret;
    }
}

package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.file.FilesStorageService;
import com.tma.english.mapper.QuestionPartTwoMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.test.part2.QuestionAndAnswerP2DTO;
import com.tma.english.models.dto.test.part2.QuestionP2CreateDTO;
import com.tma.english.models.entities.test.QuestionPartTwo;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.QuestionPartTwoRepository;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionPartTwoService {

    @Autowired
    private QuestionPartTwoRepository questionPartTwoRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionPartTwoMapper questionPartTwoMapper;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private FilesStorageService filesStorageService;

    public QuestionAndAnswerP2DTO addQuestionByTest(Long testId, QuestionP2CreateDTO questionP2CreateDTO) throws NotFoundException{
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Not found test with id: " + testId));

        QuestionPartTwo question = questionPartTwoMapper.questionP2CreateDTOToQuestionPartTwo(questionP2CreateDTO);
        question.setTest(test);

        return questionPartTwoMapper.questionPartTwoToQuestionAndAnswerP2DTO(
                questionPartTwoRepository.save(question)
        );
    }

    public List<QuestionAndAnswerP2DTO> getAll() {
        return questionPartTwoRepository.findAll()
                .stream().map(questionPartTwoMapper::questionPartTwoToQuestionAndAnswerP2DTO)
                .collect(Collectors.toList());
    }

    public QuestionAndAnswerP2DTO addQuestion(QuestionP2CreateDTO questionP2CreateDTO) {
        QuestionPartTwo question = questionPartTwoMapper.questionP2CreateDTOToQuestionPartTwo(questionP2CreateDTO);

        return questionPartTwoMapper.questionPartTwoToQuestionAndAnswerP2DTO(
                questionPartTwoRepository.save(question)
        );
    }

    public List<QuestionAndAnswerP2DTO> assignQuestionToTest(Long testId, Long questionId) throws NotFoundException {
        QuestionPartTwo question = questionPartTwoRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        if (question.getTest() != null) {
            throw new BadRequestException("This question is belonging to " + question.getTest().getTestName());
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        question.setTest(test);
        questionPartTwoRepository.save(question);

        return test.getQuestionPartTwos()
                .stream().map(questionPartTwoMapper::questionPartTwoToQuestionAndAnswerP2DTO)
                .collect(Collectors.toList());
    }

    public void deleteQuestion(Long questionId) throws NotFoundException {
        QuestionPartTwo question = questionPartTwoRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        questionPartTwoRepository.delete(question);
    }

    public QuestionAndAnswerP2DTO updateQuestion(Long questionId, QuestionP2CreateDTO questionP2CreateDTO)
            throws NotFoundException {
        QuestionPartTwo question = questionPartTwoRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

//        question.setAudio(questionP2CreateDTO.getAudio());
        question.setCorrectAnswer(questionP2CreateDTO.getCorrectAnswer());
        question.setExplanationAnswer(questionP2CreateDTO.getExplanationAnswer());
        question.setDifficultyLevel(questionP2CreateDTO.getDifficultyLevel());

        questionPartTwoRepository.save(question);
        return questionPartTwoMapper.questionPartTwoToQuestionAndAnswerP2DTO(question);
    }

    public QuestionAndAnswerP2DTO getQuestion(Long questionId) throws NotFoundException {
        QuestionPartTwo question = questionPartTwoRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        QuestionAndAnswerP2DTO response = questionPartTwoMapper.questionPartTwoToQuestionAndAnswerP2DTO(question);
        response.setTestOverviewDTO(testMapper.testToTestOverviewDTO(question.getTest()));

        return response;
    }

    public QuestionAndAnswerP2DTO uploadFile(Long id, MultipartFile audio) throws NotFoundException {
        QuestionPartTwo question = questionPartTwoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found with id " + id));

        question.setAudio(filesStorageService.uploadFile(audio, "/audio"));

        return questionPartTwoMapper.questionPartTwoToQuestionAndAnswerP2DTO(
                questionPartTwoRepository.save(question)
        );
    }
}

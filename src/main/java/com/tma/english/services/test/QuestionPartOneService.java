package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.file.FilesStorageService;
import com.tma.english.mapper.QuestionPartOneMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.test.part1.QuestionAndAnswerP1DTO;
import com.tma.english.models.dto.test.part1.QuestionP1CreateDTO;
import com.tma.english.models.entities.test.QuestionPartOne;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.QuestionPartOneRepository;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionPartOneService {

    @Autowired
    private QuestionPartOneRepository questionPartOneRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionPartOneMapper questionPartOneMapper;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private FilesStorageService filesStorageService;

    public QuestionAndAnswerP1DTO addQuestionByTest(Long testId, QuestionP1CreateDTO questionP1CreateDTO) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Not found test with id: " + testId));

        QuestionPartOne question = questionPartOneMapper.questionP1CreateDTOToQuestionPartOne(questionP1CreateDTO);
        question.setTest(test);

        return questionPartOneMapper.questionPartOneToQuestionAndAnswerP1DTO(
                questionPartOneRepository.save(question)
        );
    }

    public List<QuestionAndAnswerP1DTO> getAll() {
        return questionPartOneRepository.findAll()
                .stream().map(questionPartOneMapper::questionPartOneToQuestionAndAnswerP1DTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestionAndAnswerP1DTO addQuestion(QuestionP1CreateDTO questionP1CreateDTO) {
        QuestionPartOne question = questionPartOneMapper.questionP1CreateDTOToQuestionPartOne(questionP1CreateDTO);

        return questionPartOneMapper.questionPartOneToQuestionAndAnswerP1DTO(
                questionPartOneRepository.save(question)
        );
    }

    public List<QuestionAndAnswerP1DTO> assignQuestionToTest(Long testId, Long questionId) throws NotFoundException {
        QuestionPartOne question = questionPartOneRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        if (question.getTest() != null) {
            throw new BadRequestException("This question is belonging to " + question.getTest().getTestName());
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        question.setTest(test);
        questionPartOneRepository.save(question);

        return test.getQuestionPartOnes()
                .stream().map(questionPartOneMapper::questionPartOneToQuestionAndAnswerP1DTO)
                .collect(Collectors.toList());
    }

    public void deleteQuestion(Long questionId) throws NotFoundException {
        QuestionPartOne question = questionPartOneRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        questionPartOneRepository.delete(question);
    }

    public QuestionAndAnswerP1DTO updateQuestion(Long questionId, QuestionP1CreateDTO questionP1CreateDTO)
            throws NotFoundException {
        QuestionPartOne question = questionPartOneRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

//        question.setAudio(questionP1CreateDTO.getAudio());
//        question.setImage(questionP1CreateDTO.getImage());
        question.setCorrectAnswer(questionP1CreateDTO.getCorrectAnswer());
        question.setExplanationAnswer(questionP1CreateDTO.getExplanationAnswer());
        question.setDifficultyLevel(questionP1CreateDTO.getDifficultyLevel());

        questionPartOneRepository.save(question);
        return questionPartOneMapper.questionPartOneToQuestionAndAnswerP1DTO(question);
    }

    public QuestionAndAnswerP1DTO getQuestion(Long questionId) throws NotFoundException {
        QuestionPartOne question = questionPartOneRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        QuestionAndAnswerP1DTO ret = questionPartOneMapper.questionPartOneToQuestionAndAnswerP1DTO(question);
        ret.setTestOverviewDTO(testMapper.testToTestOverviewDTO(question.getTest()));

        return ret;
    }

    public QuestionAndAnswerP1DTO uploadFile(Long id, MultipartFile audio, MultipartFile image) throws NotFoundException {
        QuestionPartOne question = questionPartOneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found with id " + id));

        question.setAudio(filesStorageService.uploadFile(audio, "/audio"));
        question.setImage(filesStorageService.uploadFile(image, "/image"));

        return questionPartOneMapper.questionPartOneToQuestionAndAnswerP1DTO(
                questionPartOneRepository.save(question)
        );
    }
}

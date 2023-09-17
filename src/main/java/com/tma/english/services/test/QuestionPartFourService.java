package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.file.FilesStorageService;
import com.tma.english.mapper.QuestionPartFourMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.test.part4.GroupQuestionAndAnswerP4DTO;
import com.tma.english.models.dto.test.part4.GroupQuestionP4CreateDTO;
import com.tma.english.models.dto.test.part4.QuestionP4CreateDTO;
import com.tma.english.models.entities.test.GroupQuestionPartFour;
import com.tma.english.models.entities.test.QuestionPartFour;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.GroupQuestionPartFourRepository;
import com.tma.english.repositories.test.QuestionPartFourRepository;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionPartFourService {

    @Autowired
    private GroupQuestionPartFourRepository groupQuestionPartFourRepository;

    @Autowired
    private QuestionPartFourRepository questionPartFourRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private QuestionPartFourMapper questionPartFourMapper;

    @Autowired
    private FilesStorageService filesStorageService;

    public List<GroupQuestionAndAnswerP4DTO> getAll() {

        return groupQuestionPartFourRepository.findAll()
                .stream().map(q -> {
                    GroupQuestionAndAnswerP4DTO questionAndAnswerP4DTO = questionPartFourMapper.groupQuestionPartFourTogroupQuestionAndAnswerP4DTO(q);

                    questionAndAnswerP4DTO.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartFourMapper::questionPartFourToQuestionAndAnswerP4DTO)
                                    .collect(Collectors.toList())
                    );
                    questionAndAnswerP4DTO.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return questionAndAnswerP4DTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupQuestionAndAnswerP4DTO addQuestion(GroupQuestionP4CreateDTO groupQuestionP4CreateDTO) {

        if (groupQuestionP4CreateDTO.getQuestionsList().size() != 3) {
            throw new BadRequestException("There must be 3 single questions for a group");
        }

        // save to database
        GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.save(
                GroupQuestionPartFour.builder()
//                        .audio(groupQuestionP4CreateDTO.getAudio())
//                        .image(groupQuestionP4CreateDTO.getImage())
                        .questionNo(groupQuestionP4CreateDTO.getQuestionNo())
                        .transcript(groupQuestionP4CreateDTO.getTranscript())
                        .vietnameseTranscript(groupQuestionP4CreateDTO.getVietnameseTranscript())
                        .build()
        );

        List<QuestionPartFour> listQuestions = groupQuestionP4CreateDTO.getQuestionsList()
                .stream().map(questionPartFourMapper::questionP4CreateDTOToQuestionPartFour)
                .map(q -> {
                    q.setGroupQuestionPartFour(groupQuestion);
                    return questionPartFourRepository.save(q);
                }).toList();

        // construct response data

        GroupQuestionAndAnswerP4DTO response = questionPartFourMapper.groupQuestionPartFourTogroupQuestionAndAnswerP4DTO(
                groupQuestion
        );

        response.setQuestionList(
                listQuestions.stream().map(questionPartFourMapper::questionPartFourToQuestionAndAnswerP4DTO)
                        .toList()
        );

        return response;
    }

    public GroupQuestionAndAnswerP4DTO getQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        GroupQuestionAndAnswerP4DTO response =
                questionPartFourMapper.groupQuestionPartFourTogroupQuestionAndAnswerP4DTO(groupQuestion);

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartFourMapper::questionPartFourToQuestionAndAnswerP4DTO)
                        .toList()
        );

        response.setTestOverviewDTO(testMapper.testToTestOverviewDTO(groupQuestion.getTest()));

        return response;
    }

    public void deleteQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        groupQuestionPartFourRepository.delete(groupQuestion);
    }

    public GroupQuestionAndAnswerP4DTO updateQuestion(Long questionId, GroupQuestionP4CreateDTO groupQuestionP4CreateDTO)
            throws NotFoundException {
        GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

//        update data
//        groupQuestion.setAudio(groupQuestionP4CreateDTO.getAudio());
//        groupQuestion.setImage(groupQuestionP4CreateDTO.getImage());

        IntStream.range(0, groupQuestion.getQuestionsList().size())
                .forEach(i -> {
                    QuestionPartFour question = groupQuestion.getQuestionsList().get(i);
                    QuestionP4CreateDTO updateQuestion = groupQuestionP4CreateDTO.getQuestionsList().get(i);

                    question.setQuestionText(updateQuestion.getQuestionText());
                    question.setOption1(updateQuestion.getOption1());
                    question.setOption2(updateQuestion.getOption2());
                    question.setOption3(updateQuestion.getOption3());
                    question.setOption4(updateQuestion.getOption4());
                    question.setCorrectAnswer(updateQuestion.getCorrectAnswer());
                    question.setExplanationAnswer(updateQuestion.getExplanationAnswer());
                    question.setDifficultyLevel(updateQuestion.getDifficultyLevel());
                });

        groupQuestionPartFourRepository.save(groupQuestion);

//        construct response data
        GroupQuestionAndAnswerP4DTO response = questionPartFourMapper.groupQuestionPartFourTogroupQuestionAndAnswerP4DTO(
                groupQuestion
        );

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartFourMapper::questionPartFourToQuestionAndAnswerP4DTO)
                        .toList()
        );

        return response;
    }

    public Object assignQuestionToTest(Long testId, Long questionId) throws NotFoundException {
        GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        if (groupQuestion.getTest() != null) {
            throw new BadRequestException("This question is belonging to " + groupQuestion.getTest().getTestName());
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        groupQuestion.setTest(test);

        groupQuestionPartFourRepository.save(groupQuestion);

        return null;
    }

    @Transactional
    public GroupQuestionAndAnswerP4DTO addQuestionByTest(Long testId, GroupQuestionP4CreateDTO groupQuestionP4CreateDTO) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Not found test with id: " + testId));

        if (groupQuestionP4CreateDTO.getQuestionsList().size() != 3) {
            throw new BadRequestException("There must be 3 single questions for a group");
        }

        // save to database
        GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.save(
                GroupQuestionPartFour.builder()
//                        .audio(groupQuestionP4CreateDTO.getAudio())
//                        .image(groupQuestionP4CreateDTO.getImage())
                        .questionNo(groupQuestionP4CreateDTO.getQuestionNo())
                        .transcript(groupQuestionP4CreateDTO.getTranscript())
                        .vietnameseTranscript(groupQuestionP4CreateDTO.getVietnameseTranscript())
                        .build()
        );

        groupQuestion.setTest(test);

        List<QuestionPartFour> listQuestions = groupQuestionP4CreateDTO.getQuestionsList()
                .stream().map(questionPartFourMapper::questionP4CreateDTOToQuestionPartFour)
                .map(q -> {
                    q.setGroupQuestionPartFour(groupQuestion);
                    return questionPartFourRepository.save(q);
                }).toList();

        // construct response data

        GroupQuestionAndAnswerP4DTO response = questionPartFourMapper.groupQuestionPartFourTogroupQuestionAndAnswerP4DTO(
                groupQuestion
        );

        response.setQuestionList(
                listQuestions.stream().map(questionPartFourMapper::questionPartFourToQuestionAndAnswerP4DTO)
                        .toList()
        );

        return response;
    }

    public GroupQuestionAndAnswerP4DTO uploadFile(Long id, MultipartFile audio, MultipartFile image) throws NotFoundException {
        GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found with id " + id));

        groupQuestion.setAudio(filesStorageService.uploadFile(audio, "/audio"));
        if (image != null)
            groupQuestion.setImage(filesStorageService.uploadFile(image, "/image"));

        groupQuestionPartFourRepository.save(groupQuestion);

        // construct response data

        GroupQuestionAndAnswerP4DTO response = questionPartFourMapper.groupQuestionPartFourTogroupQuestionAndAnswerP4DTO(
                groupQuestion
        );

        response.setQuestionList(
                groupQuestion.getQuestionsList().stream().map(questionPartFourMapper::questionPartFourToQuestionAndAnswerP4DTO)
                        .toList()
        );

        return response;

    }
}

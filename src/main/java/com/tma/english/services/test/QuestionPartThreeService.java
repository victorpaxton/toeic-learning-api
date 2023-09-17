package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.file.FilesStorageService;
import com.tma.english.mapper.QuestionPartThreeMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.test.part3.GroupQuestionAndAnswerP3DTO;
import com.tma.english.models.dto.test.part3.GroupQuestionP3CreateDTO;
import com.tma.english.models.dto.test.part3.QuestionP3CreateDTO;
import com.tma.english.models.entities.test.GroupQuestionPartThree;
import com.tma.english.models.entities.test.QuestionPartThree;
import com.tma.english.models.entities.test.Test;
import com.tma.english.repositories.test.GroupQuestionPartThreeRepository;
import com.tma.english.repositories.test.QuestionPartThreeRepository;
import com.tma.english.repositories.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionPartThreeService {

    @Autowired
    private GroupQuestionPartThreeRepository groupQuestionPartThreeRepository;

    @Autowired
    private QuestionPartThreeRepository questionPartThreeRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private QuestionPartThreeMapper questionPartThreeMapper;

    @Autowired
    private FilesStorageService filesStorageService;

    public List<GroupQuestionAndAnswerP3DTO> getAll() {

        return groupQuestionPartThreeRepository.findAll()
                .stream().map(q -> {
                    GroupQuestionAndAnswerP3DTO questionAndAnswerP3DTO = questionPartThreeMapper.groupQuestionPartThreeTogroupQuestionAndAnswerP3DTO(q);

                    questionAndAnswerP3DTO.setQuestionList(
                            q.getQuestionsList()
                                    .stream()
                                    .map(questionPartThreeMapper::questionPartThreeToQuestionAndAnswerP3DTO)
                                    .collect(Collectors.toList())
                    );
                    questionAndAnswerP3DTO.setTestOverviewDTO(testMapper.testToTestOverviewDTO(q.getTest()));

                    return questionAndAnswerP3DTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupQuestionAndAnswerP3DTO addQuestion(GroupQuestionP3CreateDTO groupQuestionP3CreateDTO) {

        if (groupQuestionP3CreateDTO.getQuestionsList().size() != 3) {
            throw new BadRequestException("There must be 3 single questions for a group");
        }

        // save to database
        GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.save(
                GroupQuestionPartThree.builder()
                        .questionNo(groupQuestionP3CreateDTO.getQuestionNo())
                        .transcript(groupQuestionP3CreateDTO.getTranscript())
                        .vietnameseTranscript(groupQuestionP3CreateDTO.getVietnameseTranscript())
                        .build()
        );

        List<QuestionPartThree> listQuestions = groupQuestionP3CreateDTO.getQuestionsList()
                .stream().map(questionPartThreeMapper::questionP3CreateDTOToQuestionPartThree)
                .map(q -> {
                    q.setGroupQuestionPartThree(groupQuestion);
                    return questionPartThreeRepository.save(q);
                }).toList();

        // construct response data

        GroupQuestionAndAnswerP3DTO response = questionPartThreeMapper.groupQuestionPartThreeTogroupQuestionAndAnswerP3DTO(
                groupQuestion
        );

        response.setQuestionList(
            listQuestions.stream().map(questionPartThreeMapper::questionPartThreeToQuestionAndAnswerP3DTO)
                .toList()
        );

        return response;
    }

    public GroupQuestionAndAnswerP3DTO getQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        GroupQuestionAndAnswerP3DTO response =
                questionPartThreeMapper.groupQuestionPartThreeTogroupQuestionAndAnswerP3DTO(groupQuestion);

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartThreeMapper::questionPartThreeToQuestionAndAnswerP3DTO)
                        .toList()
        );

        response.setTestOverviewDTO(testMapper.testToTestOverviewDTO(groupQuestion.getTest()));

        return response;
    }

    public void deleteQuestion(Long questionId) throws NotFoundException {
        GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        groupQuestionPartThreeRepository.delete(groupQuestion);
    }

    public GroupQuestionAndAnswerP3DTO updateQuestion(Long questionId, GroupQuestionP3CreateDTO groupQuestionP3CreateDTO)
            throws NotFoundException {
        GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

//        update data
//        groupQuestion.setAudio(groupQuestionP3CreateDTO.getAudio());
//        groupQuestion.setImage(groupQuestionP3CreateDTO.getImage());

        IntStream.range(0, groupQuestion.getQuestionsList().size())
                .forEach(i -> {
                    QuestionPartThree question = groupQuestion.getQuestionsList().get(i);
                    QuestionP3CreateDTO updateQuestion = groupQuestionP3CreateDTO.getQuestionsList().get(i);

                    question.setQuestionText(updateQuestion.getQuestionText());
                    question.setOption1(updateQuestion.getOption1());
                    question.setOption2(updateQuestion.getOption2());
                    question.setOption3(updateQuestion.getOption3());
                    question.setOption4(updateQuestion.getOption4());
                    question.setCorrectAnswer(updateQuestion.getCorrectAnswer());
                    question.setExplanationAnswer(updateQuestion.getExplanationAnswer());
                    question.setDifficultyLevel(updateQuestion.getDifficultyLevel());
                });

        groupQuestionPartThreeRepository.save(groupQuestion);

//        construct response data
        GroupQuestionAndAnswerP3DTO response = questionPartThreeMapper.groupQuestionPartThreeTogroupQuestionAndAnswerP3DTO(
                groupQuestion
        );

        response.setQuestionList(
                groupQuestion.getQuestionsList()
                        .stream().map(questionPartThreeMapper::questionPartThreeToQuestionAndAnswerP3DTO)
                        .toList()
        );

        return response;
    }

    public Object assignQuestionToTest(Long testId, Long questionId) throws NotFoundException {
        GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No question found with id " + questionId));

        if (groupQuestion.getTest() != null) {
            throw new BadRequestException("This question is belonging to " + groupQuestion.getTest().getTestName());
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        groupQuestion.setTest(test);

        groupQuestionPartThreeRepository.save(groupQuestion);

        return null;
    }

    @Transactional
    public GroupQuestionAndAnswerP3DTO addQuestionByTest(Long testId, GroupQuestionP3CreateDTO groupQuestionP3CreateDTO) throws NotFoundException {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Not found test with id: " + testId));

        if (groupQuestionP3CreateDTO.getQuestionsList().size() != 3) {
            throw new BadRequestException("There must be 3 single questions for a group");
        }

        // save to database
        GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.save(
                GroupQuestionPartThree.builder()
//                        .audio(groupQuestionP3CreateDTO.getAudio())
//                        .image(groupQuestionP3CreateDTO.getImage())
                        .questionNo(groupQuestionP3CreateDTO.getQuestionNo())
                        .transcript(groupQuestionP3CreateDTO.getTranscript())
                        .vietnameseTranscript(groupQuestionP3CreateDTO.getVietnameseTranscript())
                        .build()
        );

        groupQuestion.setTest(test);

        List<QuestionPartThree> listQuestions = groupQuestionP3CreateDTO.getQuestionsList()
                .stream().map(questionPartThreeMapper::questionP3CreateDTOToQuestionPartThree)
                .map(q -> {
                    q.setGroupQuestionPartThree(groupQuestion);
                    return questionPartThreeRepository.save(q);
                })
                .toList();

        // construct response data

        GroupQuestionAndAnswerP3DTO response = questionPartThreeMapper.groupQuestionPartThreeTogroupQuestionAndAnswerP3DTO(
                groupQuestion
        );

        response.setQuestionList(
                listQuestions.stream().map(questionPartThreeMapper::questionPartThreeToQuestionAndAnswerP3DTO)
                        .toList()
        );

        return response;

    }

    public GroupQuestionAndAnswerP3DTO uploadFile(Long id, MultipartFile audio, MultipartFile image) throws NotFoundException {
        GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found with id " + id));

        groupQuestion.setAudio(filesStorageService.uploadFile(audio, "/audio"));
        if (image != null)
            groupQuestion.setImage(filesStorageService.uploadFile(image, "/image"));

        groupQuestionPartThreeRepository.save(groupQuestion);

        // construct response data

        GroupQuestionAndAnswerP3DTO response = questionPartThreeMapper.groupQuestionPartThreeTogroupQuestionAndAnswerP3DTO(
                groupQuestion
        );

        response.setQuestionList(
                groupQuestion.getQuestionsList().stream().map(questionPartThreeMapper::questionPartThreeToQuestionAndAnswerP3DTO)
                        .toList()
        );

        return response;

    }
}

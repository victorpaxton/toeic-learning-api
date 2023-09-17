package com.tma.english.services.test;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.mapper.AttemptTestMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.models.dto.attemptTest.*;
import com.tma.english.models.dto.attemptTest.request.AnswerDTO;
import com.tma.english.models.dto.attemptTest.request.SubmitTestDTO;
import com.tma.english.models.entities.attemptTest.AttemptTest;
import com.tma.english.models.entities.attemptTest.UserAnswer;
import com.tma.english.models.entities.test.*;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.models.enums.SessionType;
import com.tma.english.repositories.attemptTest.AttemptTestRepository;
import com.tma.english.repositories.attemptTest.UserAnswerRepository;
import com.tma.english.repositories.test.*;
import com.tma.english.repositories.user.UserRepository;
import com.tma.english.services.ScoreService;
import com.tma.english.services.StreakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class AttemptTestService {

    @Autowired
    private AttemptTestRepository attemptTestRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionPartOneRepository questionPartOneRepository;

    @Autowired
    private QuestionPartTwoRepository questionPartTwoRepository;

    @Autowired
    private GroupQuestionPartThreeRepository groupQuestionPartThreeRepository;

    @Autowired
    private GroupQuestionPartFourRepository groupQuestionPartFourRepository;

    @Autowired
    private QuestionPartFiveRepository questionPartFiveRepository;

    @Autowired
    private GroupQuestionPartSixRepository groupQuestionPartSixRepository;

    @Autowired
    private GroupQuestionPartSevenRepository groupQuestionPartSevenRepository;

    @Autowired
    private AttemptTestMapper attemptTestMapper;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StreakService streakService;

    @Transactional
    public AttemptTestResultDTO submitTest(Principal principal, Long testId, SubmitTestDTO submitTestDTO) throws NotFoundException {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("No test found with id " + testId));

        int attemptedNo = attemptTestRepository.findByUserAttemptAndTestAttempt(user, test).size() + 1;

        AttemptTest attemptTest = attemptTestRepository.save(AttemptTest.builder().build());

        attemptTest.setUserAttempt(user);
        attemptTest.setTestAttempt(test);
        attemptTest.setAttemptNo(attemptedNo);
        attemptTest.setCompletedAt(Instant.now());

        AtomicInteger part1CorrectCount = new AtomicInteger(0);
        AtomicInteger part2CorrectCount = new AtomicInteger(0);
        AtomicInteger part3CorrectCount = new AtomicInteger(0);
        AtomicInteger part4CorrectCount = new AtomicInteger(0);
        AtomicInteger part5CorrectCount = new AtomicInteger(0);
        AtomicInteger part6CorrectCount = new AtomicInteger(0);
        AtomicInteger part7CorrectCount = new AtomicInteger(0);

        submitTestDTO.getPart1()
                .forEach(q -> questionPartOneRepository.findById(q.getQuestionId())
                        .ifPresentOrElse(
                                question -> {
                                    UserAnswer userAnswer = UserAnswer.builder()
                                            .part(1)
                                            .questionId(q.getQuestionId())
                                            .userAnswer(q.getUserAnswer())
                                            .isCorrect(q.getUserAnswer() == question.getCorrectAnswer())
                                            .attemptTest(attemptTest)
                                            .build();

                                    userAnswerRepository.save(userAnswer);

                                    if (userAnswer.getIsCorrect())
                                        part1CorrectCount.getAndIncrement();
                                },

                                () -> {
                                    throw new BadRequestException("Question not found with id " + q.getQuestionId());
                                }
                        ));

        submitTestDTO.getPart2()
                .forEach(q -> {
                    questionPartTwoRepository.findById(q.getQuestionId())
                            .ifPresentOrElse(
                                    question -> {
                                        UserAnswer userAnswer = UserAnswer.builder()
                                                .part(2)
                                                .questionId(q.getQuestionId())
                                                .userAnswer(q.getUserAnswer())
                                                .isCorrect(q.getUserAnswer() == question.getCorrectAnswer())
                                                .attemptTest(attemptTest)
                                                .build();

                                        userAnswerRepository.save(userAnswer);

                                        if (Boolean.TRUE.equals(userAnswer.getIsCorrect()))
                                            part2CorrectCount.getAndIncrement();
                                    },
                                    () -> {
                                        throw new BadRequestException("Question not found with id " + q.getQuestionId());
                                    }
                            );
                });

        submitTestDTO.getPart3()
                .forEach(q -> groupQuestionPartThreeRepository.findById(q.getId())
                        .ifPresentOrElse(
                                groupQuestion -> groupQuestion.getQuestionsList()
                                        .stream()
                                        .forEach(question -> {

                                            AnswerDTO answerDTO = q.getQuestionList().stream().filter(e -> e.getQuestionId() == question.getId()).toList().get(0);

                                            UserAnswer userAnswer = UserAnswer.builder()
                                                    .part(3)
                                                    .groupId(groupQuestion.getId())
                                                    .questionId(question.getId())
                                                    .userAnswer(answerDTO.getUserAnswer())
                                                    .isCorrect(answerDTO.getUserAnswer() == question.getCorrectAnswer())
                                                    .attemptTest(attemptTest)
                                                    .build();

                                            userAnswerRepository.save(userAnswer);

                                            if (Boolean.TRUE.equals(userAnswer.getIsCorrect()))
                                                part3CorrectCount.getAndIncrement();

                                        }),

                                () -> {
                                    throw new BadRequestException("Question not found with id " + q.getId());
                                }
                        ));

        submitTestDTO.getPart4()
                .forEach(q -> {
                    groupQuestionPartFourRepository.findById(q.getId())
                            .ifPresentOrElse(
                                    groupQuestion -> groupQuestion.getQuestionsList()
                                            .forEach(question -> {

                                                AnswerDTO answerDTO = q.getQuestionList().stream().filter(e -> e.getQuestionId() == question.getId()).toList().get(0);

                                                UserAnswer userAnswer = UserAnswer.builder()
                                                        .part(4)
                                                        .groupId(groupQuestion.getId())
                                                        .questionId(question.getId())
                                                        .userAnswer(answerDTO.getUserAnswer())
                                                        .isCorrect(answerDTO.getUserAnswer() == question.getCorrectAnswer())
                                                        .attemptTest(attemptTest)
                                                        .build();

                                                userAnswerRepository.save(userAnswer);

                                                if (Boolean.TRUE.equals(userAnswer.getIsCorrect()))
                                                    part4CorrectCount.getAndIncrement();

                                            }),

                                    () -> {
                                        throw new BadRequestException("Question not found with id " + q.getId());
                                    }
                            );
                });

        submitTestDTO.getPart5()
                .forEach(q -> questionPartFiveRepository.findById(q.getQuestionId())
                        .ifPresentOrElse(
                                question -> {
                                    UserAnswer userAnswer = UserAnswer.builder()
                                            .part(5)
                                            .questionId(q.getQuestionId())
                                            .userAnswer(q.getUserAnswer())
                                            .isCorrect(q.getUserAnswer() == question.getCorrectAnswer())
                                            .attemptTest(attemptTest)
                                            .build();

                                    userAnswerRepository.save(userAnswer);

                                    if (Boolean.TRUE.equals(userAnswer.getIsCorrect()))
                                        part5CorrectCount.getAndIncrement();
                                },
                                () -> {
                                    throw new BadRequestException("Question not found with id " + q.getQuestionId());
                                }
                        ));

        submitTestDTO.getPart6()
                .forEach(q -> groupQuestionPartSixRepository.findById(q.getId())
                        .ifPresentOrElse(
                                groupQuestion -> groupQuestion.getQuestionsList()
                                        .forEach(question -> {

                                            AnswerDTO answerDTO = q.getQuestionList().stream().filter(e -> e.getQuestionId() == question.getId()).toList().get(0);

                                            UserAnswer userAnswer = UserAnswer.builder()
                                                    .part(6)
                                                    .groupId(groupQuestion.getId())
                                                    .questionId(question.getId())
                                                    .userAnswer(answerDTO.getUserAnswer())
                                                    .isCorrect(answerDTO.getUserAnswer() == question.getCorrectAnswer())
                                                    .attemptTest(attemptTest)
                                                    .build();

                                            userAnswerRepository.save(userAnswer);

                                            if (userAnswer.getIsCorrect())
                                                part6CorrectCount.getAndIncrement();

                                        }),

                                () -> {
                                    throw new BadRequestException("Question not found with id " + q.getId());
                                }
                        ));

        submitTestDTO.getPart7()
                .forEach(q -> groupQuestionPartSevenRepository.findById(q.getId())
                        .ifPresentOrElse(
                                groupQuestion -> groupQuestion.getQuestionsList()
                                        .forEach(question -> {

                                            AnswerDTO answerDTO = q.getQuestionList().stream().filter(e -> e.getQuestionId() == question.getId()).toList().get(0);

                                            UserAnswer userAnswer = UserAnswer.builder()
                                                    .part(7)
                                                    .groupId(groupQuestion.getId())
                                                    .questionId(question.getId())
                                                    .userAnswer(answerDTO.getUserAnswer())
                                                    .isCorrect(answerDTO.getUserAnswer() == question.getCorrectAnswer())
                                                    .attemptTest(attemptTest)
                                                    .build();

                                            userAnswerRepository.save(userAnswer);

                                            if (userAnswer.getIsCorrect())
                                                part7CorrectCount.getAndIncrement();

                                        }),

                                () -> {
                                    throw new BadRequestException("Question not found with id " + q.getId());
                                }
                        ));

        attemptTest.setPart1Correct(part1CorrectCount.get());
        attemptTest.setPart2Correct(part2CorrectCount.get());
        attemptTest.setPart3Correct(part3CorrectCount.get());
        attemptTest.setPart4Correct(part4CorrectCount.get());
        attemptTest.setPart5Correct(part5CorrectCount.get());
        attemptTest.setPart6Correct(part6CorrectCount.get());
        attemptTest.setPart7Correct(part7CorrectCount.get());

        // Set the number of correct answers and score
        int listeningCorrect = part1CorrectCount.get() + part2CorrectCount.get() + part3CorrectCount.get() + part4CorrectCount.get();
        int readingCorrect = part5CorrectCount.get() + part6CorrectCount.get() + part7CorrectCount.get();

        int listeningScore = this.calculateListeningScore(listeningCorrect);
        int readingScore = this.calculateReadingScore(readingCorrect);

        attemptTest.setListeningScore(listeningScore);
        attemptTest.setReadingScore(readingScore);
        attemptTest.setTotalScore(listeningScore + readingScore);

        AttemptTestResultDTO response = attemptTestMapper.attemptTestToAttemptTestResultDTO(attemptTest);

        // increase number of completed tests
        if (attemptedNo == 1) user.setNumOfCompletedTest(user.getNumOfCompletedTest() + 1);
        userRepository.save(user);

        // add to score table
        scoreService.addScore(user, SessionType.FULL_TEST, Long.valueOf(response.getTotalScore()));

        response.setStreakStatus(streakService.getStreakStatusToday(user));

        return response;
    }

    public AttemptResultOverviewDTO getAttemptResultOverview(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        AttemptResultOverviewDTO attemptResultOverview = AttemptResultOverviewDTO.builder()
                .part1Correct(attemptTest.getPart1Correct())
                .part2Correct(attemptTest.getPart2Correct())
                .part3Correct(attemptTest.getPart3Correct())
                .part4Correct(attemptTest.getPart4Correct())
                .part5Correct(attemptTest.getPart5Correct())
                .part6Correct(attemptTest.getPart6Correct())
                .part7Correct(attemptTest.getPart7Correct())
                .listeningScore(attemptTest.getListeningScore())
                .readingScore(attemptTest.getReadingScore())
                .totalScore(attemptTest.getTotalScore())
                .build();

        return attemptResultOverview;
    }

    public int calculateListeningScore(int numberOfCorrectAnswer) {
        int score = 5;

        if (numberOfCorrectAnswer <= 17) return score;
        if (numberOfCorrectAnswer > 93) return 495;

        score += (numberOfCorrectAnswer - 17) * 5;

        int[] bonus = {29, 30, 36, 37, 38, 40, 41, 42, 45, 46, 48, 53, 54, 57, 58, 62, 63, 75, 76, 79, 86, 89};
        int index = Arrays.binarySearch(bonus, numberOfCorrectAnswer);

        if (index < 0) {
            index = -index - 1;
            score += index * 5;
        } else {
            score += (index + 1) * 5;
        }

        return score;
    }

    public int calculateReadingScore(int numberOfCorrectAnswer) {
        int score = 5;

        if (numberOfCorrectAnswer <= 21) return score;
        if (numberOfCorrectAnswer == 100) return 495;

        score += (numberOfCorrectAnswer - 21) * 5;

        if (numberOfCorrectAnswer >= 69) score -= 5;
        if (numberOfCorrectAnswer >= 98) score -= 5;

        int[] bonus = {30, 39, 40, 47, 49, 51, 52, 53, 57, 58, 63, 71, 79, 81, 83, 86, 87, 90, 92, 95, 97};
        int index = Arrays.binarySearch(bonus, numberOfCorrectAnswer);

        if (index < 0) {
            index = -index - 1;
            score += index * 5;
        } else {
            score += (index + 1) * 5;
        }

        return score;
    }

    public List<UserAnswerResponseP1> getAttemptResultPart1(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        return userAnswerRepository.findByAttemptTestAndPart(attemptTest, 1)
                .stream()
                .map(userAnswer -> {
                    UserAnswerResponseP1 response = attemptTestMapper.questionP1ToUserAnswerResponseP1(
                            questionPartOneRepository.findById(userAnswer.getQuestionId()).get()
                    );

                    response.setUserAnswer(userAnswer.getUserAnswer());
                    response.setCorrect(userAnswer.getIsCorrect());

                    return response;
                })
                .sorted(Comparator.comparing(UserAnswerResponseP1::getQuestionNo))
                .toList();
    }

    public List<UserAnswerResponseP2> getAttemptResultPart2(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        return userAnswerRepository.findByAttemptTestAndPart(attemptTest, 2)
                .stream()
                .map(userAnswer -> {
                    UserAnswerResponseP2 response = attemptTestMapper.questionP2ToUserAnswerResponseP2(
                            questionPartTwoRepository.findById(userAnswer.getQuestionId()).get()
                    );

                    response.setUserAnswer(userAnswer.getUserAnswer());
                    response.setCorrect(userAnswer.getIsCorrect());

                    return response;
                })
                .sorted(Comparator.comparing(UserAnswerResponseP2::getQuestionNo))
                .toList();
    }

    public List<GroupUserAnswerResponseP3> getAttemptResultPart3(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        List<UserAnswer> singleAnswers =
                userAnswerRepository.findByAttemptTestAndPart(attemptTest, 3);

        List<Long> groupIds = singleAnswers.stream()
                .map(UserAnswer::getGroupId)
                .distinct()
                .toList();

        return groupIds.stream().map(groupId -> {

                    GroupQuestionPartThree groupQuestion = groupQuestionPartThreeRepository.findById(groupId).get();

                    GroupUserAnswerResponseP3 groupResponse = attemptTestMapper.groupQuestionPartThreeToGroupUserAnswerResponseP3(
                            groupQuestion
                    );

                    List<SingleQuestion> answerList = groupQuestion.getQuestionsList().stream()
                            .map((question) -> {
                                SingleQuestion marked = attemptTestMapper.questionP3ToSingleQuestion(question);

                                UserAnswer answer = singleAnswers.stream().filter(a -> a.getQuestionId() == marked.getId()).toList().get(0);

                                marked.setUserAnswer(answer.getUserAnswer());
                                marked.setCorrect(answer.getIsCorrect());

                                return marked;
                            })
                            .sorted(Comparator.comparing(SingleQuestion::getQuestionNo))
                            .toList();

                    groupResponse.setAnswerList(answerList);

                    return groupResponse;
                }).sorted(Comparator.comparing(GroupUserAnswerResponseP3::getQuestionNo))
                .toList();

    }

    public List<GroupUserAnswerResponseP3> getAttemptResultPart4(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        List<UserAnswer> singleAnswers =
                userAnswerRepository.findByAttemptTestAndPart(attemptTest, 4);

        List<Long> groupIds = singleAnswers.stream()
                .map(UserAnswer::getGroupId)
                .distinct()
                .toList();

        return groupIds.stream().map(groupId -> {

                    GroupQuestionPartFour groupQuestion = groupQuestionPartFourRepository.findById(groupId).get();

                    GroupUserAnswerResponseP3 groupResponse = attemptTestMapper.groupQuestionPartFourToGroupUserAnswerResponse(
                            groupQuestion
                    );

                    List<SingleQuestion> answerList = groupQuestion.getQuestionsList().stream()
                            .map((question) -> {
                                SingleQuestion marked = attemptTestMapper.questionP4ToSingleQuestion(question);

                                UserAnswer answer = singleAnswers.stream().filter(a -> a.getQuestionId() == marked.getId()).toList().get(0);

                                marked.setUserAnswer(answer.getUserAnswer());
                                marked.setCorrect(answer.getIsCorrect());

                                return marked;
                            })
                            .sorted(Comparator.comparing(SingleQuestion::getQuestionNo))
                            .toList();

                    groupResponse.setAnswerList(answerList);

                    return groupResponse;
                }).sorted(Comparator.comparing(GroupUserAnswerResponseP3::getQuestionNo))
                .toList();
    }

    public List<SingleQuestion> getAttemptResultPart5(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        return userAnswerRepository.findByAttemptTestAndPart(attemptTest, 5)
                .stream()
                .map(userAnswer -> {
                    SingleQuestion response = attemptTestMapper.questionP5ToUserAnswerResponseP5(
                            questionPartFiveRepository.findById(userAnswer.getQuestionId()).get()
                    );

                    response.setUserAnswer(userAnswer.getUserAnswer());
                    response.setCorrect(userAnswer.getIsCorrect());

                    return response;
                })
                .sorted(Comparator.comparing(SingleQuestion::getQuestionNo))
                .toList();
    }

    public List<GroupUserAnswerResponseP6> getAttemptResultPart6(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        List<UserAnswer> singleAnswers =
                userAnswerRepository.findByAttemptTestAndPart(attemptTest, 6);

        List<Long> groupIds = singleAnswers.stream()
                .map(UserAnswer::getGroupId)
                .distinct()
                .toList();

        return groupIds.stream().map(groupId -> {

                    GroupQuestionPartSix groupQuestion = groupQuestionPartSixRepository.findById(groupId).get();

                    GroupUserAnswerResponseP6 groupResponse = attemptTestMapper.groupQuestionPartSixToGroupUserAnswerResponse(
                            groupQuestion
                    );

                    List<SingleQuestion> answerList = groupQuestion.getQuestionsList().stream()
                            .map((question) -> {
                                SingleQuestion marked = attemptTestMapper.questionP6ToSingleQuestion(question);

                                UserAnswer answer = singleAnswers.stream().filter(a -> a.getQuestionId() == marked.getId()).toList().get(0);

                                marked.setUserAnswer(answer.getUserAnswer());
                                marked.setCorrect(answer.getIsCorrect());

                                return marked;
                            })
                            .sorted(Comparator.comparing(SingleQuestion::getQuestionNo))
                            .toList();

                    groupResponse.setAnswerList(answerList);

                    return groupResponse;
                })
                .sorted(Comparator.comparing(GroupUserAnswerResponseP6::getQuestionNo))
                .toList();
    }

    public List<GroupUserAnswerResponseP7> getAttemptResultPart7(Long attemptId) {
        AttemptTest attemptTest = attemptTestRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt test not found with id " + attemptId));

        List<UserAnswer> singleAnswers =
                userAnswerRepository.findByAttemptTestAndPart(attemptTest, 7);

        List<Long> groupIds = singleAnswers.stream()
                .map(UserAnswer::getGroupId)
                .distinct()
                .toList();

        return groupIds.stream().map(groupId -> {

                    GroupQuestionPartSeven groupQuestion = groupQuestionPartSevenRepository.findById(groupId).get();

                    GroupUserAnswerResponseP7 groupResponse = attemptTestMapper.groupQuestionPartSevenToGroupUserAnswerResponse(
                            groupQuestion
                    );

                    List<SingleQuestion> answerList = groupQuestion.getQuestionsList().stream()
                            .map(question -> {
                                SingleQuestion marked = attemptTestMapper.questionP7ToSingleQuestion(question);

                                UserAnswer answer = singleAnswers.stream().filter(a -> Objects.equals(a.getQuestionId(), marked.getId())).toList().get(0);

                                marked.setUserAnswer(answer.getUserAnswer());
                                marked.setCorrect(answer.getIsCorrect());

                                return marked;
                            })
                            .sorted(Comparator.comparing(SingleQuestion::getQuestionNo))
                            .toList();

                    groupResponse.setAnswerList(answerList);

                    return groupResponse;
                })
                .sorted(Comparator.comparing(GroupUserAnswerResponseP7::getQuestionNo))
                .toList();
    }


}

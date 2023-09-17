package com.tma.english.services.vocab;

import com.tma.english.exception.BadRequestException;
import com.tma.english.mapper.VocabMapper;
import com.tma.english.mapper.VocabTemplateMapper;
import com.tma.english.models.dto.vocab.IgnoredSettingDTO;
import com.tma.english.models.dto.vocab.UserVocabResponseDTO;
import com.tma.english.models.dto.vocab.UserVocabStatus;
import com.tma.english.models.dto.vocab.VocabCategoryResponseDTO;
import com.tma.english.models.dto.vocab.vocab_template.CompleteLessonRequestDTO;
import com.tma.english.models.dto.vocab.vocab_template.CompleteLessonResponseDTO;
import com.tma.english.models.dto.vocab.vocab_template.LearnTemplateResponseDTO;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.models.entities.vocab.UserVocabulary;
import com.tma.english.models.entities.vocab.VocabCategory;
import com.tma.english.models.enums.SessionType;
import com.tma.english.repositories.vocab.UserVocabularyRepository;
import com.tma.english.repositories.vocab.VocabCategoryRepository;
import com.tma.english.repositories.vocab.VocabularyRepository;
import com.tma.english.services.ScoreService;
import com.tma.english.services.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserVocabService {

    @Autowired
    private VocabCategoryRepository vocabCategoryRepository;

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private UserVocabularyRepository userVocabularyRepository;

    @Autowired
    private VocabMapper vocabMapper;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StreakService streakService;

    public Page<VocabCategoryResponseDTO> getAllLessons(Principal principal, String keyword, int pageNumber, int pageSize, String sortField) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        Pageable getTopicsByPageAndSortField = PageRequest.of(pageNumber, pageSize).withSort(Sort.by(sortField));

        return vocabCategoryRepository.findByCriterion(keyword, getTopicsByPageAndSortField).map(vocabCategory -> {
            VocabCategoryResponseDTO response = vocabMapper.vocabCategoryToVocabCategoryResponseDTO(vocabCategory);

            Map<String, Long> statusCount = userVocabularyRepository.countStatusWordLearningByCategory(vocabCategory.getId(), user.getId());

            response.setLearningStatus(
                    VocabCategoryResponseDTO.Status.builder()
                            .learned(statusCount.get("learned_count"))
                            .difficult(statusCount.get("difficult_count"))
                            .ignored(statusCount.get("ignored_count"))
                            .completedPercent(vocabCategory.getTotalWords() == 0 ? 0 : (int) (statusCount.get("learned_count") * 100 / vocabCategory.getTotalWords()))
                            .completedThisSession(Objects.equals(vocabCategory.getTotalWords(), statusCount.get("learned_count")))
                            .build()
            );

            return response;
        });
    }

    public Page<UserVocabResponseDTO> getLesson(Principal principal, Long categoryId, int pageNumber, int pageSize, String sortField) {
        VocabCategory category = vocabCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Vocabulary category not found with id " + categoryId));

        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        Pageable getWordsByCategoryAndSortField = PageRequest.of(pageNumber, pageSize).withSort(Sort.by(sortField));

        return userVocabularyRepository.findUserWordStatusByCategory(categoryId, user.getId(), getWordsByCategoryAndSortField)
                        .map(e -> {
                            UserVocabResponseDTO userVocabResponseDTO = vocabMapper.userVocabToUserVocabResponseDTO(e);

                            boolean isDifficult = Boolean.TRUE.equals(e.getIs_difficult());
                            boolean isIgnored = Boolean.TRUE.equals(e.getIgnored());

                            UserVocabStatus status = UserVocabStatus.builder()
                                    .lastTime(e.getLast_time())
                                    .isDifficult(isDifficult)
                                    .ignored(isIgnored)
                                    .reviewInHour(e.getLast_time() != null ? Math.max((int) ChronoUnit.HOURS.between(Instant.now(), e.getLast_time().plus(5, ChronoUnit.HOURS)), 0) : -1)
                                    .build();

                            userVocabResponseDTO.setStatus(status);
                            return userVocabResponseDTO;
                        });
    }

    public List<LearnTemplateResponseDTO> continueLearning(Principal principal, Long categoryId, Long limit) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        if (!vocabCategoryRepository.existsById(categoryId))
            throw new BadRequestException("VocabCategory not found with id " + categoryId);

        return vocabularyRepository.findUnlearnedVocabulariesByCategory(categoryId, user.getId(), limit)
                .stream().map(vocabulary -> LearnTemplateResponseDTO.builder()
                        .id(vocabulary.getId())
                        .english(vocabulary.getEnglish())
                        .definition(vocabulary.getVietnamese())
                        .presentation(VocabTemplateMapper.toVocabPresentationDTO(vocabulary.getTemplate()))
                        .screens(List.of(
                                VocabTemplateMapper.toVocabMultipleChoiceDTO(vocabulary.getTemplate()),
                                VocabTemplateMapper.toVocabReversedMultipleChoice(vocabulary.getTemplate()),
                                VocabTemplateMapper.toVocabTyping(vocabulary.getTemplate())
                        )).build()
                ).toList();
    }

    @Transactional
    public CompleteLessonResponseDTO completeLesson(Principal principal, Long categoryId, CompleteLessonRequestDTO completeLessonRequestDTO) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        VocabCategory vocabCategory = vocabCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("VocabCategory not found with id " + categoryId));

        scoreService.addScore(user, SessionType.VOCABULARY, completeLessonRequestDTO.getScoreEarned());

        completeLessonRequestDTO.getJustLearnedList()
                .forEach(justLearned -> userVocabularyRepository.findByUserIdAndWordId(user.getId(), justLearned.getVocabularyId())
                        .ifPresentOrElse(
                                userVocabulary -> {
                                    userVocabulary.setLastTime(Instant.now());
                                    userVocabulary.setIsDifficult(justLearned.getIsDifficult());
                                    userVocabulary.setIgnored(justLearned.getIgnored());
                                    userVocabularyRepository.save(userVocabulary);
                                },
                                () -> {
                                    UserVocabulary userVocabulary = UserVocabulary.builder()
                                            .lastTime(Instant.now())
                                            .isDifficult(justLearned.getIsDifficult())
                                            .ignored(justLearned.getIgnored())
                                            .word(vocabularyRepository.findById(justLearned.getVocabularyId()).get())
                                            .user(user)
                                            .build();

                                    userVocabularyRepository.save(userVocabulary);
                                }
                        ));

        VocabCategoryResponseDTO response = vocabMapper.vocabCategoryToVocabCategoryResponseDTO(vocabCategory);
        Map<String, Long> statusCount = userVocabularyRepository.countStatusWordLearningByCategory(vocabCategory.getId(), user.getId());
        response.setLearningStatus(
                VocabCategoryResponseDTO.Status.builder()
                        .learned(statusCount.get("learned_count"))
                        .difficult(statusCount.get("difficult_count"))
                        .ignored(statusCount.get("ignored_count"))
                        .completedPercent(vocabCategory.getTotalWords() == 0 ? 0 : (int) (statusCount.get("learned_count") * 100 / vocabCategory.getTotalWords()))
                        .completedThisSession(Objects.equals(vocabCategory.getTotalWords(), statusCount.get("learned_count")))
                        .build()
        );

        return CompleteLessonResponseDTO.builder()
                .scoreEarn(completeLessonRequestDTO.getScoreEarned())
                .categorySummaryStats(response)
                .streakStatus(streakService.getStreakStatusToday(user))
                .build();
    }

    public List<LearnTemplateResponseDTO> getReviewLesson(Principal principal, Long categoryId, Long limit) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        if (!vocabCategoryRepository.existsById(categoryId))
            throw new BadRequestException("VocabCategory not found with id " + categoryId);

        Instant lastTimeThreshold = Instant.now().minus(5, ChronoUnit.HOURS);

        return vocabularyRepository.findReviewVocabulariesByCategory(categoryId, user.getId(), lastTimeThreshold, limit)
                .stream().map(vocabulary -> LearnTemplateResponseDTO.builder()
                        .id(vocabulary.getId())
                        .english(vocabulary.getEnglish())
                        .definition(vocabulary.getVietnamese())
                        .presentation(VocabTemplateMapper.toVocabPresentationDTO(vocabulary.getTemplate()))
                        .screens(List.of(
                                VocabTemplateMapper.toVocabMultipleChoiceDTO(vocabulary.getTemplate()),
                                VocabTemplateMapper.toVocabReversedMultipleChoice(vocabulary.getTemplate()),
                                VocabTemplateMapper.toVocabTyping(vocabulary.getTemplate())
                        )).build()
                ).toList();
    }

    public void ignoredSetting(Principal principal, Long categoryId, List<IgnoredSettingDTO> ignoredRequest) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        if (!vocabCategoryRepository.existsById(categoryId))
            throw new BadRequestException("VocabCategory not found with id " + categoryId);

        ignoredRequest.forEach(e -> {
            if (!userVocabularyRepository.existsByWordIdAndUserId(e.getWordId(), user.getId())) {
                userVocabularyRepository.save(
                        UserVocabulary.builder()
                                .isDifficult(false)
                                .ignored(e.isIgnored())
                                .user(user)
                                .word(vocabularyRepository.findById(e.getWordId()).get())
                                .build()
                );
            } else {
                userVocabularyRepository.ignoredSetting(e.getWordId(), user.getId(), e.isIgnored());
            }
        });
    }
}

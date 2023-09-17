package com.tma.english.repositories.vocab;

import com.tma.english.models.entities.vocab.UserVocabulary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Long> {
    List<UserVocabulary> findByUserId(Long user);

    Optional<UserVocabulary> findByUserIdAndWordId(Long userId, Long wordId);

    boolean existsByWordIdAndUserId(Long wordId, Long userId);

    @Query(value = "SELECT " +
            "(SELECT COUNT(*) FROM user_vocabulary uv WHERE uv.last_time IS NOT NULL AND uv.user_id = :userId AND EXISTS (SELECT 1 FROM vocabulary v WHERE v.id = uv.vocabulary_id AND v.category_id = :categoryId)) AS learned_count, " +
            "(SELECT COUNT(*) FROM user_vocabulary uv WHERE uv.is_difficult = 'true' AND uv.user_id = :userId AND EXISTS (SELECT 1 FROM vocabulary v WHERE v.id = uv.vocabulary_id AND v.category_id = :categoryId)) AS difficult_count, " +
            "(SELECT COUNT(*) FROM user_vocabulary uv WHERE uv.ignored = 'true' AND uv.user_id = :userId AND EXISTS (SELECT 1 FROM vocabulary v WHERE v.id = uv.vocabulary_id AND v.category_id = :categoryId)) AS ignored_count",
            nativeQuery = true)
    Map<String, Long> countStatusWordLearningByCategory(@Param("categoryId") Long categoryId,
                                                           @Param("userId") Long userId);


    @Query(value = "SELECT COUNT(*) " +
            "FROM user_vocabulary uv " +
            "WHERE uv.last_time IS NOT NULL " +
            "  AND DATE(uv.last_time) = CURRENT_DATE" +
            "  AND uv.user_id = :userId",
            nativeQuery = true)
    Long countNumberLearnedInDay(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) " +
            "FROM user_vocabulary uv " +
            "WHERE uv.last_time IS NOT NULL " +
            "  AND DATE_TRUNC('day', uv.last_time) = DATE_TRUNC('day', CURRENT_DATE - INTERVAL '1 day') " +
            "  AND uv.user_id = :userId", nativeQuery = true)
    Long countNumberLearnedYesterday(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_vocabulary " +
            "SET ignored = :ignored " +
            "WHERE user_id = :userId " +
            "AND vocabulary_id = :wordId", nativeQuery = true)
    void ignoredSetting(@Param("wordId") Long wordId,
                        @Param("userId") Long userId,
                        @Param("ignored") Boolean ignored);

    @Query(value = "SELECT v.id, english, vietnamese, audio, video, image, pronunciation, attributes, last_time, is_difficult, ignored " +
            "FROM vocabulary v " +
            "LEFT JOIN user_vocabulary uv ON v.id = uv.vocabulary_id AND uv.user_id = :userId " +
            "WHERE v.category_id = :categoryId",
    nativeQuery = true)
    Page<UserVocab> findUserWordStatusByCategory(@Param("categoryId") Long categoryId,
                                                 @Param("userId") Long userId,
                                                 Pageable pageable);

    interface UserVocab {
        Long getId();
        String getEnglish();
        String getVietnamese();
        String getAudio();
        String getVideo();
        String getImage();
        String getPronunciation();
        String getAttributes();
        Instant getLast_time();
        Boolean getIs_difficult();
        Boolean getIgnored();
    }

}

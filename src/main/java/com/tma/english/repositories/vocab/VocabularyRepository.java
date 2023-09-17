package com.tma.english.repositories.vocab;

import com.tma.english.models.entities.vocab.Vocabulary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    Page<Vocabulary> findByVocabCategoryId(Long id, Pageable pageable);

    @Query(value = "SELECT * FROM vocabulary v " +
            "WHERE v.category_id = :categoryId " +
            "AND NOT EXISTS " +
            "(SELECT * FROM user_vocabulary uv " +
            "WHERE uv.vocabulary_id = v.id " +
            "AND uv.user_id = :userId " +
            "AND (uv.last_time is not null OR uv.ignored = 'true')) " +
            "LIMIT :limit", nativeQuery = true)
    List<Vocabulary> findUnlearnedVocabulariesByCategory(@Param("categoryId") Long categoryId,
                                                         @Param("userId") Long userId,
                                                         @Param("limit") Long limit);

    @Query(value = "SELECT v.* " +
            "FROM vocabulary v " +
            "JOIN user_vocabulary uv ON v.id = uv.vocabulary_id " +
            "WHERE v.category_id = :categoryId " +
            "AND uv.user_id = :userId " +
            "AND uv.last_time is not null " +
            "AND uv.ignored = 'false' " +
            "AND uv.last_time < :lastTimeThreshold " +
            "ORDER BY uv.last_time ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Vocabulary> findReviewVocabulariesByCategory(@Param("categoryId") Long categoryId,
                                                      @Param("userId") Long userId,
                                                      @Param("lastTimeThreshold") Instant lastTimeThreshold,
                                                      @Param("limit") Long limit);
}

package com.tma.english.repositories;

import com.tma.english.models.entities.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    @Query(value = "SELECT COUNT(*) " +
            "FROM score s " +
            "WHERE DATE(s.created_at) = CURRENT_DATE " +
            "AND s.score_type in ('TEST', 'SHORT_TEST', 'PRACTICE') " +
            "AND s.user_id = :userId ",
            nativeQuery = true)
    Long countTestAttemptInDay(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) " +
            "FROM score s " +
            "WHERE DATE_TRUNC('day', uv.last_time) = DATE_TRUNC('day', CURRENT_DATE - INTERVAL '1 day') " +
            "AND s.score_type in ('TEST', 'SHORT_TEST', 'PRACTICE') " +
            "AND s.user_id = :userId ", nativeQuery = true)
    Long countTestAttemptYesterday(@Param("userId") Long userId);

    @Query(value = "SELECT t.user_id, u.first_name, u.last_name, u.profile_picture, t.total_score, " +
            "RANK() OVER (ORDER BY total_score desc) " +
            "FROM (SELECT user_id, " +
            "SUM(score) AS total_score " +
            "FROM score " +
            "WHERE score_type = :scoreType " +
            "AND date_trunc(:period, created_at) = date_trunc(:period, current_date) " +
            "GROUP BY user_id) AS t " +
            "JOIN _user AS u ON t.user_id = u.id",
            countQuery = "select count(*) from (SELECT user_id, " +
                    "SUM(score) AS total_score " +
                    "FROM score " +
                    "WHERE score_type = :scoreType " +
                    "AND date_trunc(:period, created_at) = date_trunc(:period, current_date) " +
                    "GROUP BY user_id) AS t",
            nativeQuery = true)
    Page<Ranking> getLeaderboard(@Param("scoreType") String scoreType,
                                 @Param("period") String period,
                                 Pageable pageable);


    @Query(value = "SELECT t.user_id, t.total_score, " +
            "RANK() OVER (ORDER BY total_score desc) " +
            "FROM (SELECT user_id, " +
            "SUM(score) AS total_score " +
            "FROM score " +
            "WHERE score_type = :scoreType " +
            "AND date_trunc(:period, created_at) = date_trunc(:period, current_date) " +
            "GROUP BY user_id) AS t " +
            "WHERE user_id = :userId",
            countQuery = "select count(*) from (SELECT user_id, " +
                    "SUM(score) AS total_score " +
                    "FROM score " +
                    "WHERE score_type = :scoreType " +
                    "AND date_trunc(:period, created_at) = date_trunc(:period, current_date) " +
                    "GROUP BY user_id) AS t",
            nativeQuery = true)
    YourRank getSessionUserRanking(@Param("scoreType") String scoreType,
                           @Param("period") String period,
                           @Param("userId") Long userId);

    interface Ranking {
        Long getRank();
        Long getUser_id();
        String getFirst_name();
        String getLast_name();
        String getProfile_picture();
        Long getTotal_score();
    }

    interface YourRank {
        Long getRank();
        Long getUser_id();
        Long getTotal_score();
    }
}


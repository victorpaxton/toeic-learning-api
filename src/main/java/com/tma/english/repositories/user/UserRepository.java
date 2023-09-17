package com.tma.english.repositories.user;

import com.tma.english.models.entities.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findById(Long id);

    @Modifying
    @Query(value = "update _user set streaks = :streak where id = :userId", nativeQuery = true)
    void resetUserStreak(@Param("streak") Integer streak,
                         @Param("userId") Long userId);
}


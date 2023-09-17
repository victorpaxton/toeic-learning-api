package com.tma.english.repositories.attemptTest;

import com.tma.english.models.entities.attemptTest.AttemptTest;
import com.tma.english.models.entities.attemptTest.UserAnswer;
import com.tma.english.models.entities.test.Test;
import com.tma.english.models.entities.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptTestRepository extends JpaRepository<AttemptTest, Long> {
    List<AttemptTest> findByUserAttemptAndTestAttempt(AppUser user, Test test);
}

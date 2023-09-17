package com.tma.english.repositories.attemptTest;

import com.tma.english.mapper.AttemptTestMapper;
import com.tma.english.models.entities.attemptTest.AttemptTest;
import com.tma.english.models.entities.attemptTest.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByAttemptTestAndPart(AttemptTest attemptTest, int part);
}

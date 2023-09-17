package com.tma.english.repositories.test;

import com.tma.english.models.entities.test.QuestionPartFive;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPartFiveRepository extends JpaRepository<QuestionPartFive, Long> {
}

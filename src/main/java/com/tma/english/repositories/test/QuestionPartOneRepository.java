package com.tma.english.repositories.test;

import com.tma.english.models.entities.test.QuestionPartOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPartOneRepository extends JpaRepository<QuestionPartOne, Long> {
}

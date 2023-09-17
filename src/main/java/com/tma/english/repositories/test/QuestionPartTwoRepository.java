package com.tma.english.repositories.test;

import com.tma.english.models.entities.test.QuestionPartTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPartTwoRepository extends JpaRepository<QuestionPartTwo, Long> {
}

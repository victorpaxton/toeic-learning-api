package com.tma.english.repositories.test;

import com.tma.english.models.entities.test.QuestionPartFour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPartFourRepository extends JpaRepository<QuestionPartFour, Long> {
}

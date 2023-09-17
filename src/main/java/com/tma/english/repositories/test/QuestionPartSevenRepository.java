package com.tma.english.repositories.test;

import com.tma.english.models.entities.test.QuestionPartSeven;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPartSevenRepository extends JpaRepository<QuestionPartSeven, Long> {
}

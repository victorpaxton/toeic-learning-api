package com.tma.english.repositories.test;

import com.tma.english.models.entities.test.QuestionPartThree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPartThreeRepository extends JpaRepository<QuestionPartThree, Long> {
}

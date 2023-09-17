package com.tma.english.repositories.vocab;

import com.tma.english.models.entities.vocab.VocabLearningTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabLearningTemplateRepository extends JpaRepository<VocabLearningTemplate, Long> {
    List<VocabLearningTemplate> findByWordId(Long id);
}

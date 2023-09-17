package com.tma.english.repositories.vocab;

import com.tma.english.models.entities.vocab.VocabCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabCategoryRepository extends JpaRepository<VocabCategory, Long> {
    @Query(value = "SELECT * FROM vocab_category AS vc " +
            "WHERE CONCAT(vc.category_name, ' ', vc.image, ' ', vc.total_words) " +
            "LIKE %:keyword%", nativeQuery = true)
    Page<VocabCategory> findByCriterion(@Param("keyword") String keyword, Pageable pageable);
}

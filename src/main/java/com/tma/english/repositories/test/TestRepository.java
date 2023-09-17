package com.tma.english.repositories.test;

import com.tma.english.models.entities.test.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    @Query(value = "SELECT * FROM test AS t " +
            "WHERE CONCAT(t.id, ' ', t.test_name, ' ', t.description) " +
            "LIKE %:keyword%", nativeQuery = true)
    Page<Test> findByCriterion(@Param("keyword") String keyword, Pageable pageable);
}

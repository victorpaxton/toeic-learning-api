package com.tma.english.models.entities.test;

import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "question_part2")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionPartTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_no")
    private int questionNo;

    @Column(name = "question_audio")
    private String audio;

    @Column(name = "correct_answer")
    private int correctAnswer;

    @Column(name = "explanation_answer", columnDefinition = "TEXT")
    private String explanationAnswer;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
}

package com.tma.english.models.entities.test;

import com.tma.english.models.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "question_part4")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionPartFour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_no")
    private int questionNo;

    @Column(name = "question_text")
    private String questionText;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    @Column(name = "correct_answer")
    private int correctAnswer;

    @Column(name = "explanation_answer", columnDefinition = "TEXT")
    private String explanationAnswer;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private GroupQuestionPartFour groupQuestionPartFour;
}

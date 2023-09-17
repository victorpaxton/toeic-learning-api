package com.tma.english.models.entities.attemptTest;

import jakarta.persistence.*;
import lombok.*;

@Data
@Table(name = "user_answer")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_part")
    private int part;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "question_id")
    private Long questionId;

    private int userAnswer;

    @Column(name = "is_correct")
    @Getter
    @Setter
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", referencedColumnName = "id")
    private AttemptTest attemptTest;
}

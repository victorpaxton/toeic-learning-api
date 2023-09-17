package com.tma.english.models.entities.attemptTest;

import com.tma.english.models.entities.test.Test;
import com.tma.english.models.entities.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Table(name = "attempt_test")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttemptTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int part1Correct;
    private int part2Correct;
    private int part3Correct;
    private int part4Correct;
    private int part5Correct;
    private int part6Correct;
    private int part7Correct;

    private int listeningScore;
    private int readingScore;

    private int totalScore;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "attempt_no")
    private int attemptNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser userAttempt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test testAttempt;

    @OneToMany(mappedBy = "attemptTest", fetch = FetchType.LAZY)
    private List<UserAnswer> userAnswers;
}

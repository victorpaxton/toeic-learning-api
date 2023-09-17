package com.tma.english.models.entities.test;

import com.tma.english.models.entities.attemptTest.AttemptTest;
import com.tma.english.models.entities.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "test")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_name")
    private String testName;
    private String description;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "timer_in_minutes")
    private int timerInMinutes;

    @Column(name = "num_of_participants")
    private int numOfParticipants;

    @OneToMany(mappedBy = "test")
    private List<QuestionPartOne> questionPartOnes;

    @OneToMany(mappedBy = "test")
    private List<QuestionPartTwo> questionPartTwos;

    @OneToMany(mappedBy = "test")
    private List<GroupQuestionPartThree> groupQuestionPartThrees;

    @OneToMany(mappedBy = "test")
    private List<GroupQuestionPartFour> groupQuestionPartFours;

    @OneToMany(mappedBy = "test")
    private List<QuestionPartFive> questionPartFives;

    @OneToMany(mappedBy = "test")
    private List<GroupQuestionPartSix> groupQuestionPartSixes;

    @OneToMany(mappedBy = "test")
    private List<GroupQuestionPartSeven> groupQuestionPartSevens;

    @OneToMany(mappedBy = "testAttempt")
    private List<AttemptTest> attemptTests;
}

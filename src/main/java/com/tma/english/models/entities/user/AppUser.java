package com.tma.english.models.entities.user;

import com.tma.english.models.entities.Badge;
import com.tma.english.models.entities.BaseEntity;
import com.tma.english.models.entities.Score;
import com.tma.english.models.entities.attemptTest.AttemptTest;
import com.tma.english.models.entities.vocab.UserVocabulary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Column(name = "first_name")
    private String firstName = "Your";

    @Column(name = "last_name")
    private String lastName = "Name";

    private String phone = "";

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "num_of_completed_courses")
    private Integer numOfCompletedTest;

    @Column(name = "vocab_daily_goals")
    private Integer vocabDailyGoals = 5;     // number of vocab to learn daily

    @Column(name = "test_daily_goals")
    private Integer testDailyGoals = 1;      // number of short or full test to complete daily

    private Integer streaks;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

    private String roles;

    private boolean enabled = Boolean.TRUE;

    @OneToMany(mappedBy = "userAttempt", fetch = FetchType.EAGER)
    private List<AttemptTest> attemptTests;

    @OneToMany(mappedBy = "user")
    private List<UserVocabulary> userVocabularyList;

    @OneToMany(mappedBy = "user")
    private List<Score> scoreList;

    @OneToMany(mappedBy = "user")
    private List<Badge> badgeList;
}



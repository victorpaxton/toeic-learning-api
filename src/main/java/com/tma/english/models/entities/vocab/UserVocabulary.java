package com.tma.english.models.entities.vocab;

import com.tma.english.models.entities.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(name = "user_vocabulary")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_time")
    private Instant lastTime;

    @Column(name = "is_difficult")
    private Boolean isDifficult;

    private Boolean ignored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", referencedColumnName = "id")
    private Vocabulary word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

}

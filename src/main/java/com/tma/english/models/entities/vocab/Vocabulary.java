package com.tma.english.models.entities.vocab;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "vocabulary")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String english;
    private String vietnamese;
    private String audio;
    private String video;
    private String image;
    private String pronunciation;
    private String attributes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonIgnore
    private VocabCategory vocabCategory;

    @OneToMany(mappedBy = "word")
    @JsonIgnore
    private List<UserVocabulary> userVocabularyList;

    @OneToOne(mappedBy = "word")
    @JsonIgnore
    private VocabLearningTemplate template;
}

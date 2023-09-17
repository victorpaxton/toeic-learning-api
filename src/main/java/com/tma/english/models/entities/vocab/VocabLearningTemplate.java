package com.tma.english.models.entities.vocab;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@Table(name = "vocab_learning_template")
@NoArgsConstructor
@Builder
public class VocabLearningTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "english_word_choices")
    private String englishChoices;

    @Column(name = "vietnamese_choices")
    private String vietnameseChoices;

    @Column(name = "letter_choices")
    private String letterChoices;

    @OneToOne
    @JoinColumn(name = "vocabulary_id", referencedColumnName = "id")
    private Vocabulary word;


}

package com.tma.english.models.entities.vocab;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "vocab_category")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class VocabCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    private String image;

    @Column(name = "total_words")
    private Long totalWords;

    @OneToMany(mappedBy = "vocabCategory", cascade = CascadeType.REMOVE)
    private List<Vocabulary> vocabularies;

}

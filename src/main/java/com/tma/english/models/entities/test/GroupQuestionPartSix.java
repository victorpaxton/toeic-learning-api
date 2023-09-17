package com.tma.english.models.entities.test;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "group_question_part6")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupQuestionPartSix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_no")
    private int questionNo;

    @Column(name = "passage_text", columnDefinition = "TEXT")
    private String passageText;

    @Column(name = "passage_type")
    private String passageType;

    @Column(name = "in_vietnamese", columnDefinition = "TEXT")
    private String inVietnamese;

    @OneToMany(mappedBy = "groupQuestionPartSix")
    private List<QuestionPartSix> questionsList;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
}

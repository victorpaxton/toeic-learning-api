package com.tma.english.models.entities.test;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "group_question_part4")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupQuestionPartFour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_no")
    private int questionNo;

    @Column(name = "question_audio")
    private String audio;

    @Column(name = "question_image")
    private String image;

    @Column(name = "transcript", columnDefinition = "TEXT")
    private String transcript;

    @Column(name = "vietnamese_transcript", columnDefinition = "TEXT")
    private String vietnameseTranscript;

    @OneToMany(mappedBy = "groupQuestionPartFour")
    private List<QuestionPartFour> questionsList;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
}

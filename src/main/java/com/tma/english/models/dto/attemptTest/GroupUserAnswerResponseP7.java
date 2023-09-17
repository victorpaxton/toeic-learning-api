package com.tma.english.models.dto.attemptTest;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUserAnswerResponseP7 {
    private Long id;
    private int questionNo;
    private int numOfPassages;
    private String passageText;
    private String passageType;
    private String image;

    private List<SingleQuestion> answerList;
}

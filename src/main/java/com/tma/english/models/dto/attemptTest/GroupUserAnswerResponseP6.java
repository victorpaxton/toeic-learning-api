package com.tma.english.models.dto.attemptTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUserAnswerResponseP6 {
    private Long id;
    private int questionNo;
    private String passageText;
    private String passageType;

    private List<SingleQuestion> answerList;
}

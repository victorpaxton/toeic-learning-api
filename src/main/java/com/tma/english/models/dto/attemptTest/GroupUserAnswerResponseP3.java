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
public class GroupUserAnswerResponseP3 {

    private Long id;
    private int questionNo;
    private String audio;
    private String image;

    private List<SingleQuestion> answerList;
}

package com.tma.english.models.dto.attemptTest.request;

import com.tma.english.models.dto.attemptTest.request.AnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupAnswerDTO {
    private Long id;
    private List<AnswerDTO> questionList;
}

package com.tma.english.models.dto.attemptTest.request;

import com.tma.english.models.dto.attemptTest.request.AnswerDTO;
import com.tma.english.models.dto.attemptTest.request.GroupAnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitTestDTO {
    private List<AnswerDTO> part1;
    private List<AnswerDTO> part2;
    private List<GroupAnswerDTO> part3;
    private List<GroupAnswerDTO> part4;
    private List<AnswerDTO> part5;
    private List<GroupAnswerDTO> part6;
    private List<GroupAnswerDTO> part7;
}
package com.tma.english.mapper;

import com.tma.english.models.dto.test.part1.QuestionAndAnswerP1DTO;
import com.tma.english.models.dto.test.part1.QuestionP1CreateDTO;
import com.tma.english.models.dto.test.part1.QuestionP1ResponseDTO;
import com.tma.english.models.entities.test.QuestionPartOne;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionPartOneMapper {
    QuestionPartOne questionP1CreateDTOToQuestionPartOne(QuestionP1CreateDTO questionP1CreateDTO);

    QuestionP1ResponseDTO questionPartOneToQuestionP1ResponseDTO(QuestionPartOne questionPartOne);

    QuestionAndAnswerP1DTO questionPartOneToQuestionAndAnswerP1DTO(QuestionPartOne questionPartOne);
}

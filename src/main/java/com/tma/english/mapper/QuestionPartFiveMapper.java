package com.tma.english.mapper;

import com.tma.english.models.dto.test.part1.QuestionAndAnswerP1DTO;
import com.tma.english.models.dto.test.part1.QuestionP1CreateDTO;
import com.tma.english.models.dto.test.part1.QuestionP1ResponseDTO;
import com.tma.english.models.dto.test.part5.QuestionAndAnswerP5DTO;
import com.tma.english.models.dto.test.part5.QuestionP5CreateDTO;
import com.tma.english.models.dto.test.part5.QuestionP5ResponseDTO;
import com.tma.english.models.entities.test.QuestionPartFive;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionPartFiveMapper {
    QuestionPartFive questionP5CreateDTOToQuestionPartFive(QuestionP5CreateDTO questionP5CreateDTO);

    QuestionP5ResponseDTO questionPartFiveToQuestionP5ResponseDTO(QuestionPartFive questionPartFive);

    QuestionAndAnswerP5DTO questionPartFiveToQuestionAndAnswerP5DTO(QuestionPartFive questionPartFive);
}

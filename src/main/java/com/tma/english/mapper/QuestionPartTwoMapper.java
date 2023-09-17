package com.tma.english.mapper;

import com.tma.english.models.dto.test.part2.QuestionAndAnswerP2DTO;
import com.tma.english.models.dto.test.part2.QuestionP2CreateDTO;
import com.tma.english.models.dto.test.part2.QuestionP2ResponseDTO;
import com.tma.english.models.entities.test.QuestionPartTwo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionPartTwoMapper {
    QuestionPartTwo questionP2CreateDTOToQuestionPartTwo(QuestionP2CreateDTO questionP2CreateDTO);

    QuestionP2ResponseDTO questionPartTwoToQuestionP2ResponseDTO(QuestionPartTwo questionPartTwo);

    QuestionAndAnswerP2DTO questionPartTwoToQuestionAndAnswerP2DTO(QuestionPartTwo questionPartTwo);
}

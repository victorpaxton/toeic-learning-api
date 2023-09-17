package com.tma.english.mapper;

import com.tma.english.models.dto.test.part7.*;
import com.tma.english.models.entities.test.GroupQuestionPartSeven;
import com.tma.english.models.entities.test.QuestionPartSeven;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionPartSevenMapper {
    public GroupQuestionAndAnswerP7DTO groupQuestionPartSevenToGroupQuestionAndAnswerP7DTO(GroupQuestionPartSeven groupQuestionPartSeven);

    public QuestionAndAnswerP7DTO questionPartSevenToQuestionAndAnswerP7DTO(QuestionPartSeven questionPartSeven);

    public QuestionPartSeven questionP7CreateDTOToQuestionPartSeven(QuestionP7CreateDTO questionP7CreateDTO);

    public GroupQuestionP7DTO groupQuestionPartSevenToGroupQuestionP7DTO(GroupQuestionPartSeven groupQuestionPartSeven);

    public QuestionP7DTO questionPartSevenToQuestionP7DTO(QuestionPartSeven questionPartSeven);
}

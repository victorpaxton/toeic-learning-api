package com.tma.english.mapper;

import com.tma.english.models.dto.test.part4.*;
import com.tma.english.models.entities.test.GroupQuestionPartFour;
import com.tma.english.models.entities.test.QuestionPartFour;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionPartFourMapper {

    public GroupQuestionAndAnswerP4DTO groupQuestionPartFourTogroupQuestionAndAnswerP4DTO(GroupQuestionPartFour groupQuestionPartFour);

    public QuestionAndAnswerP4DTO questionPartFourToQuestionAndAnswerP4DTO(QuestionPartFour questionPartFour);

    public QuestionPartFour questionP4CreateDTOToQuestionPartFour(QuestionP4CreateDTO questionP4CreateDTO);

    public QuestionP4DTO questionPartFourToQuestionP4DTO(QuestionPartFour questionPartFour);

    public GroupQuestionP4DTO groupQuestionPartFourToGroupQuestionP4DTO(GroupQuestionPartFour groupQuestionPartFour);

}

package com.tma.english.mapper;

import com.tma.english.models.dto.test.part3.*;
import com.tma.english.models.entities.test.GroupQuestionPartThree;
import com.tma.english.models.entities.test.QuestionPartThree;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionPartThreeMapper {
    public GroupQuestionAndAnswerP3DTO groupQuestionPartThreeTogroupQuestionAndAnswerP3DTO(GroupQuestionPartThree groupQuestionPartThree);

    public QuestionAndAnswerP3DTO questionPartThreeToQuestionAndAnswerP3DTO(QuestionPartThree questionPartThree);

    public QuestionPartThree questionP3CreateDTOToQuestionPartThree(QuestionP3CreateDTO questionP3CreateDTO);

    public GroupQuestionP3DTO groupQuestionPartThreeToGroupQuestionP3DTO(GroupQuestionPartThree groupQuestionPartThree);

    public QuestionP3DTO questionPartThreeToQuestionP3DTO(QuestionPartThree questionPartThree);
}

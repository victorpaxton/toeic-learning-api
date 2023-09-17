package com.tma.english.mapper;

import com.tma.english.models.dto.test.part4.GroupQuestionAndAnswerP4DTO;
import com.tma.english.models.dto.test.part4.GroupQuestionP4CreateDTO;
import com.tma.english.models.dto.test.part4.QuestionAndAnswerP4DTO;
import com.tma.english.models.dto.test.part4.QuestionP4CreateDTO;
import com.tma.english.models.dto.test.part6.*;
import com.tma.english.models.entities.test.GroupQuestionPartSix;
import com.tma.english.models.entities.test.QuestionPartSix;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionPartSixMapper {
    public GroupQuestionAndAnswerP6DTO groupQuestionPartSixToGroupQuestionAndAnswerP6DTO(GroupQuestionPartSix groupQuestionPartSix);

    public QuestionAndAnswerP6DTO questionPartSixToQuestionAndAnswerP6DTO(QuestionPartSix questionPartSix);

    public QuestionPartSix questionP6CreateDTOToQuestionPartSix(QuestionP6CreateDTO questionP6CreateDTO);

    public GroupQuestionP6DTO groupQuestionPartSixToGroupQuestionP6DTO(GroupQuestionPartSix groupQuestionPartSix);

    public QuestionP6DTO questionPartSixToQuestionP6DTO(QuestionPartSix questionPartSix);
}

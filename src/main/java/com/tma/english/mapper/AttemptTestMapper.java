package com.tma.english.mapper;

import com.tma.english.models.dto.attemptTest.*;
import com.tma.english.models.entities.test.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface AttemptTestMapper {
    AttemptTestResultDTO attemptTestToAttemptTestResultDTO(com.tma.english.models.entities.attemptTest.AttemptTest attemptTest);

    UserAnswerResponseP1 questionP1ToUserAnswerResponseP1(QuestionPartOne question);

    UserAnswerResponseP2 questionP2ToUserAnswerResponseP2(QuestionPartTwo question);

    GroupUserAnswerResponseP3 groupQuestionPartThreeToGroupUserAnswerResponseP3(GroupQuestionPartThree groupQuestionPartThree);
    SingleQuestion questionP3ToSingleQuestion(QuestionPartThree question);

    GroupUserAnswerResponseP3 groupQuestionPartFourToGroupUserAnswerResponse(GroupQuestionPartFour groupQuestionPartFour);
    SingleQuestion questionP4ToSingleQuestion(QuestionPartFour question);

    SingleQuestion questionP5ToUserAnswerResponseP5(QuestionPartFive question);

    GroupUserAnswerResponseP6 groupQuestionPartSixToGroupUserAnswerResponse(GroupQuestionPartSix groupQuestionPartSix);
    SingleQuestion questionP6ToSingleQuestion(QuestionPartSix question);

    GroupUserAnswerResponseP7 groupQuestionPartSevenToGroupUserAnswerResponse(GroupQuestionPartSeven groupQuestionPartSeven);
    SingleQuestion questionP7ToSingleQuestion(QuestionPartSeven question);

}

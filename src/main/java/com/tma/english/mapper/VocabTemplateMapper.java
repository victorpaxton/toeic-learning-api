package com.tma.english.mapper;

import com.tma.english.models.dto.vocab.vocab_template.VocabMultipleChoiceDTO;
import com.tma.english.models.dto.vocab.vocab_template.VocabPresentationDTO;
import com.tma.english.models.entities.vocab.VocabLearningTemplate;
import com.tma.english.models.enums.VocabLessonTemplate;

import java.util.Arrays;
import java.util.List;

public class VocabTemplateMapper {
    public static VocabPresentationDTO toVocabPresentationDTO(VocabLearningTemplate template) {
        List<String> attributes = Arrays.stream(template.getWord().getAttributes().split(";"))
                .toList();

        return VocabPresentationDTO.builder()
                .id(template.getId())
                .template(VocabLessonTemplate.PRESENTATION.name())
                .english(template.getWord().getEnglish())
                .definition(template.getWord().getVietnamese())
                .pronunciation(template.getWord().getPronunciation())
                .attributes(attributes)
                .audio(template.getWord().getAudio())
                .video(template.getWord().getVideo())
                .image(template.getWord().getImage())
                .build();
    }

    public static VocabMultipleChoiceDTO toVocabMultipleChoiceDTO(VocabLearningTemplate template) {
        List<String> choices = Arrays.stream(template.getEnglishChoices().split(";")).toList();
        List<String> attributes = Arrays.stream(template.getWord().getAttributes().split(";")).toList();

        return VocabMultipleChoiceDTO.builder()
                .id(template.getId())
                .template(VocabLessonTemplate.MULTIPLE_CHOICES.name())
                .answer(template.getWord().getEnglish())
                .choices(choices)
                .prompt(
                        VocabMultipleChoiceDTO.Prompt.builder()
                                .reversedAnswer(template.getWord().getVietnamese())
                                .pronunciation(template.getWord().getPronunciation())
                                .attributes(attributes)
                                .audio(template.getWord().getAudio())
                                .video(template.getWord().getVideo())
                                .image(template.getWord().getImage())
                                .build()
                )
                .build();
    }

    public static VocabMultipleChoiceDTO toVocabReversedMultipleChoice(VocabLearningTemplate template) {
        List<String> choices = Arrays.stream(template.getVietnameseChoices().split(";")).toList();
        List<String> attributes = Arrays.stream(template.getWord().getAttributes().split(";")).toList();

        return VocabMultipleChoiceDTO.builder()
                .id(template.getId())
                .template(VocabLessonTemplate.REVERSED_MULTIPLE_CHOICES.name())
                .answer(template.getWord().getVietnamese())
                .choices(choices)
                .prompt(
                        VocabMultipleChoiceDTO.Prompt.builder()
                                .reversedAnswer(template.getWord().getEnglish())
                                .pronunciation(template.getWord().getPronunciation())
                                .attributes(attributes)
                                .audio(template.getWord().getAudio())
                                .video(template.getWord().getVideo())
                                .image(template.getWord().getImage())
                                .build()
                )
                .build();
    }

    public static VocabMultipleChoiceDTO toVocabTyping(VocabLearningTemplate template) {
        List<String> choices = Arrays.stream(template.getLetterChoices().split(";")).toList();
        List<String> attributes = Arrays.stream(template.getWord().getAttributes().split(";")).toList();

        return VocabMultipleChoiceDTO.builder()
                .id(template.getId())
                .template(VocabLessonTemplate.TYPING.name())
                .answer(template.getWord().getEnglish().toUpperCase())
                .choices(choices)
                .prompt(
                        VocabMultipleChoiceDTO.Prompt.builder()
                                .reversedAnswer(template.getWord().getVietnamese())
                                .pronunciation(template.getWord().getPronunciation())
                                .attributes(attributes)
                                .audio(template.getWord().getAudio())
                                .video(template.getWord().getVideo())
                                .image(template.getWord().getImage())
                                .build()
                )
                .build();
    }
}

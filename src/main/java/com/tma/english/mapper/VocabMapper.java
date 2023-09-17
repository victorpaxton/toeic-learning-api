package com.tma.english.mapper;

import com.tma.english.models.dto.vocab.AddNewWordDTO;
import com.tma.english.models.dto.vocab.UserVocabResponseDTO;
import com.tma.english.models.dto.vocab.VocabCategoryBasicDTO;
import com.tma.english.models.dto.vocab.VocabCategoryResponseDTO;
import com.tma.english.models.entities.vocab.VocabCategory;
import com.tma.english.models.entities.vocab.Vocabulary;
import com.tma.english.repositories.vocab.UserVocabularyRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VocabMapper {
    VocabCategoryResponseDTO vocabCategoryToVocabCategoryResponseDTO(VocabCategory vocabCategory);

    Vocabulary addNewWordDTOToVocabulary(AddNewWordDTO addNewWordDTO);

    VocabCategoryBasicDTO vocabCategoryToVocabCategoryBasicDTO(VocabCategory vocabCategory);

    UserVocabResponseDTO userVocabToUserVocabResponseDTO(UserVocabularyRepository.UserVocab userVocab);

}

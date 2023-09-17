package com.tma.english.services.vocab;

import com.tma.english.exception.BadRequestException;
import com.tma.english.file.FilesStorageService;
import com.tma.english.mapper.VocabMapper;
import com.tma.english.mapper.VocabTemplateMapper;
import com.tma.english.models.dto.vocab.AddNewWordDTO;
import com.tma.english.models.dto.vocab.VocabCategoryBasicDTO;
import com.tma.english.models.dto.vocab.VocabCategoryCreateDTO;
import com.tma.english.models.dto.vocab.vocab_template.LearnTemplateResponseDTO;
import com.tma.english.models.dto.vocab.vocab_template.VocabTemplateCreateDTO;
import com.tma.english.models.entities.vocab.VocabCategory;
import com.tma.english.models.entities.vocab.VocabLearningTemplate;
import com.tma.english.models.entities.vocab.Vocabulary;
import com.tma.english.repositories.vocab.UserVocabularyRepository;
import com.tma.english.repositories.vocab.VocabCategoryRepository;
import com.tma.english.repositories.vocab.VocabLearningTemplateRepository;
import com.tma.english.repositories.vocab.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VocabService {
    @Autowired
    private VocabCategoryRepository vocabCategoryRepository;

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private UserVocabularyRepository userVocabularyRepository;

    @Autowired
    private VocabLearningTemplateRepository vocabLearningTemplateRepository;

    @Autowired
    private VocabMapper vocabMapper;

    @Autowired
    private FilesStorageService filesStorageService;

    public List<VocabCategoryBasicDTO> getAllCategories() {
        return vocabCategoryRepository.findAll()
                .stream().map(vocabMapper::vocabCategoryToVocabCategoryBasicDTO)
                .toList();
    }

    public VocabCategory createNewVocabCategory(VocabCategoryCreateDTO vocabCategoryCreateDTO) {
        VocabCategory category = VocabCategory.builder()
                .categoryName(vocabCategoryCreateDTO.getCategoryName())
                .totalWords(0L)
                .build();

        return vocabCategoryRepository.save(category);
    }

    @Transactional
    public Vocabulary addNewWord(Long categoryId, AddNewWordDTO addNewWordDTO) {
        return vocabCategoryRepository.findById(categoryId)
                .map(vocabCategory -> {
                    Vocabulary newWord = vocabMapper.addNewWordDTOToVocabulary(addNewWordDTO);
                    newWord.setVocabCategory(vocabCategory);

                    vocabCategory.setTotalWords(vocabCategory.getTotalWords() + 1);

                    return vocabularyRepository.save(newWord);
                })
                .orElseThrow(() -> new BadRequestException("Vocabulary category not found with id " + categoryId));
    }

    public VocabCategory getLesson(Long categoryId) {
        return vocabCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Vocabulary category not found with id " + categoryId));
    }

    public VocabCategoryBasicDTO updateTopic(Long categoryId, VocabCategoryCreateDTO vocabCategoryCreateDTO) {
        return vocabCategoryRepository.findById(categoryId)
                .map(vocabCategory -> {
                    vocabCategory.setCategoryName(vocabCategoryCreateDTO.getCategoryName());
                    return vocabMapper.vocabCategoryToVocabCategoryBasicDTO(vocabCategoryRepository.save(vocabCategory));
                }).orElseThrow(() -> new BadRequestException("Vocabulary category not found with id " + categoryId));
    }

    public void deleteTopic(Long categoryId) {
        VocabCategory category = vocabCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Vocabulary category not found with id " + categoryId));

        vocabCategoryRepository.delete(category);
    }

    public LearnTemplateResponseDTO createTemplate(Long wordId, VocabTemplateCreateDTO vocabTemplateCreateDTO) {
        Vocabulary word = vocabularyRepository.findById(wordId)
                .orElseThrow(() -> new BadRequestException("Could not find vocabulary with id " + wordId));

        VocabLearningTemplate template = vocabLearningTemplateRepository.save(
                VocabLearningTemplate.builder()
                        .englishChoices(String.join(";", vocabTemplateCreateDTO.getEnglishChoices()))
                        .vietnameseChoices(String.join(";", vocabTemplateCreateDTO.getVietnameseChoices()))
                        .letterChoices(String.join(";", vocabTemplateCreateDTO.getLetterChoices()))
                        .word(word)
                        .build()
        );

        return LearnTemplateResponseDTO.builder()
                .id(wordId)
                .english(word.getEnglish())
                .definition(word.getVietnamese())
                .presentation(VocabTemplateMapper.toVocabPresentationDTO(template))
                .screens(List.of(
                        VocabTemplateMapper.toVocabMultipleChoiceDTO(template),
                        VocabTemplateMapper.toVocabReversedMultipleChoice(template),
                        VocabTemplateMapper.toVocabTyping(template)
                )).build();
    }


    public VocabCategoryBasicDTO uploadCategoryImage(Long categoryId, MultipartFile image) {
        VocabCategory category = vocabCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Vocab category not found with id " + categoryId));

        category.setImage(filesStorageService.uploadFile(image, "/vocab-category"));

        vocabCategoryRepository.save(category);

        return vocabMapper.vocabCategoryToVocabCategoryBasicDTO(category);
    }


    public Vocabulary uploadWordImage(Long wordId, MultipartFile image) {
        Vocabulary vocabulary = vocabularyRepository.findById(wordId)
                .orElseThrow(() -> new BadRequestException("Vocabulary not found with id " + wordId));

        vocabulary.setImage(filesStorageService.uploadFile(image, "/vocabulary"));

        return vocabularyRepository.save(vocabulary);
    }

    public Vocabulary uploadWordAudio(Long wordId, MultipartFile audio) {
        Vocabulary vocabulary = vocabularyRepository.findById(wordId)
                .orElseThrow(() -> new BadRequestException("Vocabulary not found with id " + wordId));

        vocabulary.setImage(filesStorageService.uploadFile(audio, "/vocabulary"));

        return vocabularyRepository.save(vocabulary);
    }

    public Vocabulary uploadWordVideo(Long wordId, MultipartFile video) {
        Vocabulary vocabulary = vocabularyRepository.findById(wordId)
                .orElseThrow(() -> new BadRequestException("Vocabulary not found with id " + wordId));

        vocabulary.setImage(filesStorageService.uploadFile(video, "/vocabulary"));

        return vocabularyRepository.save(vocabulary);
    }
}

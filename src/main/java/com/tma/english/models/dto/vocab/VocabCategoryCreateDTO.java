package com.tma.english.models.dto.vocab;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VocabCategoryCreateDTO {
    @Size(min = 5, max = 255, message = "Category name is too short or long")
    private String categoryName;
}

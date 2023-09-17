package com.tma.english.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    @NotBlank(message = "email is required")
    @Email
    @Schema(example = "nhoangthinh@tma.com.vn")
    private String email;
}

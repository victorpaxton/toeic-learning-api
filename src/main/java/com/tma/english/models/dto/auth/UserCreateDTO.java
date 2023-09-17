package com.tma.english.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserCreateDTO {

    @NotBlank(message = "email is required")
    @Email
    @Schema(example = "nhoangthinh@tma.com.vn")
    private String email;

    @Schema(example = "Abc@1234")
    @Size(min = 8, message = "password should be at least 8 digits")
    private String password;
}


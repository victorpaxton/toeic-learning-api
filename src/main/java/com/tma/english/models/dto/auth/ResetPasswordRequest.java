package com.tma.english.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    @Schema(example = "Abc@1234")
    @Size(min = 8, message = "password should be at least 8 digits")
    private String newPassword;
}

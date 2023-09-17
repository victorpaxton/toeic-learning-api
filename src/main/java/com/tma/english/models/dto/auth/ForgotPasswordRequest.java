package com.tma.english.models.dto.auth;

import com.tma.english.validation.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {

    @Schema(example = "nhoangthinh@tma.com.vn")
    @Email
    private String email;
}

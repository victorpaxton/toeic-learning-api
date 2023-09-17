package com.tma.english.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Schema(example = "nhoangthinh@tma.com.vn")
    private String email;

    @Schema(example = "Abc@1234")
    private String password;
}

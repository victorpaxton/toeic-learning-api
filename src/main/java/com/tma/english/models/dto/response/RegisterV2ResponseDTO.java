package com.tma.english.models.dto.response;

import com.tma.english.models.dto.auth.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterV2ResponseDTO {
    private UserResponseDTO user;
    private TokenResponse tokens;


    @Data
    @Builder
    public static class TokenResponse {
        private TokenDTO access;
        private TokenDTO refresh;
    }
}

package com.tma.english.models.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tma.english.models.dto.auth.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {
    private UserResponseDTO user;

    private TokenDTO access;

    private TokenDTO refresh;
}

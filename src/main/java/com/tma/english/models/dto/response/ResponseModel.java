package com.tma.english.models.dto.response;

import com.tma.english.models.dto.auth.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ResponseModel<T, E> {
    private boolean isSuccess;
    private T response;
    private E errors;
}

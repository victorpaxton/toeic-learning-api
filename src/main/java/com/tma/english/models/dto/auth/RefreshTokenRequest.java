package com.tma.english.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCIsImVtYWlsIjoibmhvYW5ndGhpbmhAdG1hLmNvbS52biIsInR5cGUiOiJBQ0NFU1MiLCJpYXQiOjE2ODgxMDgyODIsImV4cCI6MTY4ODEwODMxMn0.NODnMQQTUHv7ctfn9mhAQvDnUHwbyb3-fMSvg-5R6BY")
    private String token;
}

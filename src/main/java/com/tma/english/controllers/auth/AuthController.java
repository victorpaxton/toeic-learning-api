package com.tma.english.controllers.auth;

import com.tma.english.mapper.UserMapper;
import com.tma.english.models.dto.auth.*;
import com.tma.english.models.dto.response.*;
import com.tma.english.models.entities.token.Token;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.models.enums.TokenType;
import com.tma.english.services.AppUserService;
import com.tma.english.services.TokenService;
import com.tma.english.utils.JwtService;
import com.tma.english.utils.OTPService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication")
@Slf4j
public class AuthController {

    @Autowired
    private AppUserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account created, verify your email to continue"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity register(@RequestBody @Valid UserCreateDTO userCreateDTO) throws MessagingException, UnsupportedEncodingException {
        AppUser user = userService.addUser(userCreateDTO);

        String token = OTPService.createRandomOneTimePassword();
        tokenService.saveOneTimeLoginToken(user, token, Instant.now().plus(15, ChronoUnit.MINUTES));

        userService.sendOTPEmail(userCreateDTO.getEmail(), token);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        try {
            authentication = authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid username or password") {
            };
        }

        if (authentication.isAuthenticated()) {
            SessionUser userDetails = (SessionUser) authentication.getPrincipal();

            Token refreshToken = tokenService.createRefreshToken(userDetails);

            String accessToken = jwtService.generateToken(userDetails);

            RegisterV2ResponseDTO response = RegisterV2ResponseDTO.builder()
                    .user(UserMapper.entityToUserResponseDto(userDetails.getUserInfo()))
                    .tokens(RegisterV2ResponseDTO.TokenResponse.builder()
                            .access(new TokenDTO(accessToken, jwtService.extractExpiration(accessToken).toInstant()))
                            .refresh(new TokenDTO(refreshToken.getToken(), jwtService.extractExpiration(refreshToken.getToken()).toInstant()))
                            .build())
                    .build();

            return ResponseModel.builder()
                    .isSuccess(true)
                    .response(response)
                    .build();

        } else {
            throw new UsernameNotFoundException("User does not exist");
        }
    }

    @PostMapping(value = "/logout")
    @Operation(summary = "Logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout successfully"),
            @ApiResponse(responseCode = "401", description = "Please authenticate",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    public ResponseEntity logout(@RequestBody RefreshTokenRequest refreshTokenRequest)
            throws AuthenticationException {
        Token refreshToken = tokenService.findByRefreshToken(refreshTokenRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token or user related to it is not found"));

        tokenService.deleteToken(refreshToken);
        SecurityContextHolder.clearContext();

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/refresh-tokens")
    @Operation(summary = "Refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token successfully"),
            @ApiResponse(responseCode = "401", description = "Please authenticate",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseModel<JwtResponse, Void>> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) throws AuthenticationException {

        Token refreshToken = tokenService.findByRefreshToken(refreshTokenRequest.getToken())
                .orElseThrow(() -> new AuthenticationException("Refresh token not found") {});

        AppUser user = tokenService.verifyExpired(refreshToken).getUser();

        String newAccessToken = jwtService.generateTokenByUser(user);

        long remaining = jwtService.extractExpiration(refreshTokenRequest.getToken()).getTime() - new Date().getTime();
        String newRefreshToken = jwtService.regenerateRefreshTokenByUser(user, remaining);

        tokenService.updateRefreshToken(refreshToken, newRefreshToken);

        JwtResponse jwtResponse = JwtResponse.builder()
                .access(new TokenDTO(newAccessToken, jwtService.extractExpiration(newAccessToken).toInstant()))
                .refresh(new TokenDTO(newRefreshToken, jwtService.extractExpiration(newRefreshToken).toInstant()))
                .build();

        ResponseModel<JwtResponse, Void> response = ResponseModel.<JwtResponse, Void>builder()
                .isSuccess(true)
                .response(jwtResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/forgot-password")
    @Operation(summary = "Forgot password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Send email successfully"),
            @ApiResponse(responseCode = "400", description = "Email not found",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    public ResponseEntity forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest)
            throws MessagingException, UnsupportedEncodingException {

        AppUser user = userService.findByEmail(forgotPasswordRequest.getEmail());

        userService.sendResetPasswordEmail(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reset password successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid reset password token",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    public ResponseEntity resetPassword(@RequestParam("token") String token,
                                                       @RequestBody ResetPasswordRequest resetPasswordRequest)
            throws AuthenticationException {
        Token resetToken = tokenService.getToken(token, TokenType.RESET_PASSWORD);

        if (resetToken.getExpireDate().isBefore(Instant.now())) {
            tokenService.deleteToken(resetToken);
            throw new AuthenticationException("Expired code") {};
        }

        tokenService.deleteToken(resetToken);
        userService.resetPassword(resetToken.getUser(), resetPasswordRequest.getNewPassword());

        return ResponseEntity.noContent().build();
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + request.getContextPath();
    }

    @PostMapping("/otp/login")
    @Operation(summary = "Login or register new account with email use OTP code")
    public ResponseEntity passwordLessLogin(@RequestBody @Valid EmailDTO emailDTO) {
        userService.passwordLessLogin(emailDTO.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/otp/verify")
    @Operation(summary = "Verify OTP code")
    public ResponseModel verifyToken(@RequestParam("token") String token, @RequestBody @Valid EmailDTO emailDTO) {
        AppUser user = userService.verifyLoginToken(token, emailDTO);

        SessionUser userDetails = new SessionUser(user);

        Token refreshToken = tokenService.createRefreshToken(userDetails);

        String accessToken = jwtService.generateToken(userDetails);

        RegisterV2ResponseDTO response = RegisterV2ResponseDTO.builder()
                .user(UserMapper.entityToUserResponseDto(userDetails.getUserInfo()))
                .tokens(RegisterV2ResponseDTO.TokenResponse.builder()
                        .access(new TokenDTO(accessToken, jwtService.extractExpiration(accessToken).toInstant()))
                        .refresh(new TokenDTO(refreshToken.getToken(), jwtService.extractExpiration(refreshToken.getToken()).toInstant()))
                        .build())
                .build();

        return ResponseModel.builder()
                .isSuccess(true)
                .response(response)
                .build();
    }

}

package com.tma.english.services;

import com.tma.english.models.entities.token.Token;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.models.enums.TokenType;
import com.tma.english.repositories.token.TokenRepository;
import com.tma.english.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    public Token createRefreshToken(SessionUser user) {
        String token = jwtService.generateRefreshToken(user);

        Token refreshToken = Token.builder()
                .token(token)
                .type(TokenType.REFRESH)
                .user(user.getUserInfo())
                .blacklisted(false)
                .expireDate(jwtService.extractExpiration(token).toInstant())
                .build();

        return tokenRepository.save(refreshToken);
    }

    public Optional<Token> findByRefreshToken(String token) {
        return tokenRepository.findByTokenAndType(token, TokenType.REFRESH);
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }

    public Token verifyExpired(Token token)  {
        try {
            jwtService.isTokenExpired(token.getToken());
            return token;
        } catch (Exception e) {
            tokenRepository.delete(token);
            throw new AuthenticationException("Expired token") {};
        }
    }

    public void updateRefreshToken(Token refreshToken, String newRefreshToken) {
        refreshToken.setToken(newRefreshToken);
        tokenRepository.save(refreshToken);
    }

    public Token getToken(String token, TokenType type) {
        return tokenRepository.findByTokenAndType(token, type)
                .orElseThrow(() -> new AuthenticationException("Invalid token") {});
    }

    public void saveResetToken(String token, AppUser user, Instant expireTime) {
        Token resetPasswordToken =
                Token.builder()
                        .token(token)
                        .user(user)
                        .type(TokenType.RESET_PASSWORD)
                        .expireDate(expireTime)
                        .build();

        tokenRepository.save(resetPasswordToken);
    }

    public Optional<Token> findByUserAndType(AppUser user, TokenType type) {
        return tokenRepository.findByUserAndType(user, type);
    }

    public Token saveOneTimeLoginToken(AppUser user, String token, Instant expireTime) {
        Token oneTimeLoginToken =
                Token.builder()
                        .token(token)
                        .user(user)
                        .type(TokenType.ONE_TIME_LOGIN)
                        .expireDate(expireTime)
                        .build();

        return tokenRepository.save(oneTimeLoginToken);
    }

}

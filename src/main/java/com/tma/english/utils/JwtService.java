package com.tma.english.utils;

import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.models.enums.TokenType;
import com.tma.english.repositories.token.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Autowired
    private TokenRepository tokenRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    @Value("${spring.application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${spring.application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Value("${spring.application.security.jwt.reset-token.expiration}")
    private long resetExpiration;

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(
            SessionUser extraClaims
    ) {
        return buildToken(extraClaims, jwtExpiration, TokenType.ACCESS);
    }

    public String generateTokenByUser(AppUser user) {
        return buildTokenByUser(user, jwtExpiration, TokenType.ACCESS);
    }


    public String generateRefreshToken(
            SessionUser extraClaims
    ) {
        return buildToken(extraClaims, refreshExpiration, TokenType.REFRESH);
    }

    public String generateRefreshTokenByUser(AppUser user) {
        return buildTokenByUser(user, refreshExpiration, TokenType.REFRESH);
    }

    public String regenerateRefreshTokenByUser(AppUser user, long remaining) {
        return buildTokenByUser(user, remaining, TokenType.REFRESH);
    }

    public String generateResetTokenByUser(AppUser user) {
        return buildTokenByUser(user, resetExpiration, TokenType.RESET_PASSWORD);
    }

    public String generateOneTimeLoginTokenByUser(AppUser user) {
        return buildTokenByUser(user, resetExpiration, TokenType.ONE_TIME_LOGIN);
    }


    private String buildToken(
            SessionUser extraClaims,
            long expiration,
            TokenType tokenType
    ) {
        AppUser user = extraClaims.getUserInfo();
        return Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("type", tokenType)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String buildTokenByUser(
            AppUser user,
            long expiration,
            TokenType tokenType
    ) {
        return Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("type", tokenType)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }
}


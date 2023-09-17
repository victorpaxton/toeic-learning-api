package com.tma.english.repositories.token;

import com.tma.english.models.entities.token.Token;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenAndType(String token, TokenType type);

    Optional<Token> findByUserAndType(AppUser user, TokenType type);
}

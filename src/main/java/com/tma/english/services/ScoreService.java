package com.tma.english.services;

import com.tma.english.models.entities.Score;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.enums.SessionType;
import com.tma.english.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    public Score addScore(AppUser user, SessionType sessionType, Long score) {
        return scoreRepository.save(Score.builder().user(user).sessionType(sessionType).score(score).build());
    }

}

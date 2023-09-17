package com.tma.english.services;

import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class LeaderboardService {
    @Autowired
    private ScoreRepository scoreRepository;

    public Page<ScoreRepository.Ranking> getLeaderboard(int pageNumber, int pageSize, String sessionType, String period) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (period.equals("all")) {
            return scoreRepository.getLeaderboard(sessionType, "century", pageable);
        }

        return scoreRepository.getLeaderboard(sessionType, period, pageable);
    }

    public ScoreRepository.YourRank getYourRank(Principal principal, String sessionType, String period) {

        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        if (period.equals("all")) {
            return scoreRepository.getSessionUserRanking(sessionType, "century", user.getId());
        }

        return scoreRepository.getSessionUserRanking(sessionType, period, user.getId());
    }
}

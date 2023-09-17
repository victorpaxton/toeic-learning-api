package com.tma.english.services;

import com.tma.english.models.dto.StreakResponseDTO;
import com.tma.english.models.entities.Badge;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.enums.SessionType;
import com.tma.english.repositories.BadgeRepository;
import com.tma.english.repositories.ScoreRepository;
import com.tma.english.repositories.user.UserRepository;
import com.tma.english.repositories.vocab.UserVocabularyRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class StreakService {

    @Autowired
    private UserVocabularyRepository userVocabularyRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public StreakResponseDTO getStreakStatusToday(AppUser user) {
        Long learnedInDay = userVocabularyRepository.countNumberLearnedInDay(user.getId());
        StreakResponseDTO.GoalStatus vocabStatus =
                StreakResponseDTO.GoalStatus.builder()
                        .goal(user.getVocabDailyGoals())
                        .learned(learnedInDay)
                        .achieved(learnedInDay.intValue() == user.getVocabDailyGoals())
                        .build();

        Long testedInDay = scoreRepository.countTestAttemptInDay(user.getId());
        StreakResponseDTO.GoalStatus testStatus =
                StreakResponseDTO.GoalStatus.builder()
                        .goal(user.getTestDailyGoals())
                        .learned(testedInDay)
                        .achieved(testedInDay.intValue() == user.getTestDailyGoals())
                        .build();

        if (vocabStatus.getAchieved() && testStatus.getAchieved())
            userRepository.resetUserStreak(user.getStreaks() + 1, user.getId());

        return StreakResponseDTO.builder()
                .vocabStatus(vocabStatus)
                .testStatus(testStatus)
                .achiveStreakToday(vocabStatus.getAchieved() && testStatus.getAchieved())
                .streakLength(user.getStreaks())
                .build();
    }

    @Scheduled(cron = "1 0 0 * * ?")
    public void checkStreakStatus() {
        userRepository.findAll().forEach(user -> {
            if (!(userVocabularyRepository.countNumberLearnedYesterday(user.getId()).intValue() == user.getVocabDailyGoals()
                    && scoreRepository.countTestAttemptYesterday(user.getId()).intValue() == user.getTestDailyGoals())) {
                if (user.getStreaks() >= 7) {
                    Badge badge = Badge.builder()
                            .sessionType(SessionType.VOCABULARY)
                            .streakLong(user.getStreaks())
                            .user(user)
                            .build();
                    badgeRepository.save(badge);
                }

                userRepository.resetUserStreak(0, user.getId());

                try {
                    sendRemindEmail(user.getEmail());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void sendRemindEmail(String email) throws MessagingException, UnsupportedEncodingException {
        String subject = "[TOEIC THINHSUY] Keep your streak!";
        String senderName = "Toeic Thinhsuy";

        String mailContent =
                "<div style=\"max-width:500px; margin: auto; padding: 20px; font-family: Arial; font-size:24px; color: #211e4f; text-align:center\">\n" +
                        "      <img src=\"https://i.imgur.com/BQxFZcI.png\" alt=\"Toeic Thinhsuy\" style=\"width:50%\">\n" +
                        "      <p style=\"margin-bottom: 25px\"> You lost streak yesterday \uD83D\uDE25</p>" +
                        "       <p style=\"margin-bottom: 15px\"> \uD83D\uDD25 Keep your streak to reach your goal soon \uD83D\uDD25 </p>\n" +
                        "      <a href='https://example.com/'><button style=\"background-color: #1560bd; color: #fff; font-weight:bold; font-size:24px; border-radius: 50px; padding: 12px 22px; cursor: pointer\"> Learn now </button></a>\n" +
                        " </div>\n";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("toeic-thinhsuy@tma.com.vn", senderName);
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        mailSender.send(message);
    }
}

package com.tma.english.mapper;

import com.tma.english.models.dto.auth.UserCreateDTO;
import com.tma.english.models.dto.auth.UserResponseDTO;
import com.tma.english.models.entities.user.AppUser;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class UserMapper {
    public static AppUser userCreateDtoToEntity(UserCreateDTO userCreateDTO) {
        AppUser appUser = AppUser.builder()
                .email(userCreateDTO.getEmail())
                .password(userCreateDTO.getPassword())
                .profilePicture("/avatar/default.jpg")
                .numOfCompletedTest(0)
                .firstName("Your")
                .lastName("Name")
                .phone("")
                .isPremium(false)
                .testDailyGoals(1)
                .vocabDailyGoals(5)
                .streaks(0)
                .roles("ROLE_USER")
                .enabled(true)
                .build();
        return appUser;
    }

    public static UserResponseDTO entityToUserResponseDto(AppUser user) {
        boolean newUser = ChronoUnit.DAYS.between(user.getCreatedAt().toInstant(), Instant.now()) < 5 || user.getNumOfCompletedTest() < 5;

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .profilePicture(user.getProfilePicture())
                .numOfCompletedTest(user.getNumOfCompletedTest())
                .vocabDailyGoals(user.getVocabDailyGoals())
                .testDailyGoals(user.getTestDailyGoals())
                .streaks(user.getStreaks())
                .isPremium(user.getIsPremium())
                .isNewUser(newUser)
                .build();

        return userResponseDTO;

    }
}

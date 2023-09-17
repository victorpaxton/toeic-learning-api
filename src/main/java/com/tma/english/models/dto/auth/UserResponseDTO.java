package com.tma.english.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    @Schema(example = "1")
    private long id;

    @Schema(example = "nhoangthinh@tma.com.vn")
    private String email;

    @Schema(example = "Nguyen Hoang")
    private String firstName;

    @Schema(example = "Thinh")
    private String lastName;

    @Schema(example = "0923675928")
    private String phone;

    @Schema(example = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
    private String profilePicture;

    private int numOfCompletedTest;

    private Integer vocabDailyGoals = 5;     // number of vocab to learn daily

    private Integer testDailyGoals = 1;      // number of short or full test to complete daily

    private Integer streaks;

    private Boolean isPremium = false;

    private boolean isNewUser;

//    @Schema(example = "ROLE_USER")
//    private String roles;

//    @Schema(example = "true")
//    private boolean enabled;
}

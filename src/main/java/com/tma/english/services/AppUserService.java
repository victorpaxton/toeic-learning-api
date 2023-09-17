package com.tma.english.services;

import com.tma.english.exception.BadRequestException;
import com.tma.english.exception.NotFoundException;
import com.tma.english.file.FilesStorageService;
import com.tma.english.mapper.AttemptTestMapper;
import com.tma.english.mapper.TestMapper;
import com.tma.english.mapper.UserMapper;
import com.tma.english.models.dto.StreakResponseDTO;
import com.tma.english.models.dto.UpdateGoalDTO;
import com.tma.english.models.dto.attemptTest.AttemptTestResultDTO;
import com.tma.english.models.dto.auth.EmailDTO;
import com.tma.english.models.dto.auth.UserCreateDTO;
import com.tma.english.models.dto.auth.UserResponseDTO;
import com.tma.english.models.dto.user.UpdateProfileDTO;
import com.tma.english.models.entities.Badge;
import com.tma.english.models.entities.token.Token;
import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.models.enums.TokenType;
import com.tma.english.repositories.BadgeRepository;
import com.tma.english.repositories.user.UserRepository;
import com.tma.english.utils.JwtService;
import com.tma.english.utils.OTPService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private AttemptTestMapper attemptTestMapper;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private StreakService streakService;

    @Autowired
    private BadgeRepository badgeRepository;

    public AppUser addUser(UserCreateDTO userCreateDTO) {
        if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        userCreateDTO.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        return userRepository.save(UserMapper.userCreateDtoToEntity(userCreateDTO));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::entityToUserResponseDto).toList();
    }

    public UserResponseDTO getUserById(Long id) throws NotFoundException {
        return userRepository.findById(id).map(UserMapper::entityToUserResponseDto)
                .orElseThrow(() -> new NotFoundException("No user found with id "));
    }

    public AppUser findByEmail(String email) throws BadRequestException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found"));
    }
    
    public void sendResetPasswordEmail(AppUser user) throws MessagingException, UnsupportedEncodingException {
        tokenService.findByUserAndType(user, TokenType.RESET_PASSWORD)
                .ifPresent(tokenService::deleteToken);

        String subject = "[TOEIC THINHSUY] Reset Password";
        String senderName = "Toeic Thinhsuy";

        String resetToken = OTPService.createRandomOneTimePassword();

        tokenService.saveResetToken(resetToken, user, Instant.now().plus(15, ChronoUnit.MINUTES));

        String mailContent =
                "<div style=\"max-width:500px; margin: auto; padding: 20px; font-family: Arial; font-size:24px; color: #211e4f; text-align:center\">\n" +
                "      <img src=\"https://i.imgur.com/BQxFZcI.png\" alt=\"Toeic Thinhsuy\" style=\"width:50%\">\n" +
                "      <p style=\"margin-bottom: 25px\">Welcome to Toeic Thinhsuy!</p>" +
                "       <p style=\"margin-bottom: 15px\"> Simply use this code to reset your password. Your OTP code will expire in 15 minutes, and can only be used once.</p>\n" +
                "      <p style=\"color: #1560bd; font-weight:bold; font-size:48px\">" + resetToken + "</p>\n" +
                " </div>\n";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("toeic-thinhsuy@tma.com.vn", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        mailSender.send(message);
    }

    public void resetPassword(AppUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public AppUser addUserV2(String email) {

        AppUser user = AppUser.builder()
                .email(email)
                .profilePicture("/avatar/default.jpg")
                .numOfCompletedTest(0)
                .roles("ROLE_USER")
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public void passwordLessLogin(String email) {
        // Check whether this email already have an account
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        // Already have an account
                        user -> {
                            // Delete any exist token before sending new one
                            tokenService.findByUserAndType(user, TokenType.ONE_TIME_LOGIN)
                                    .ifPresent(tokenService::deleteToken);

                            // Generate new OTP
                            String token = OTPService.createRandomOneTimePassword();
                            tokenService.saveOneTimeLoginToken(user, token, Instant.now().plus(15, ChronoUnit.MINUTES));

                            try {
                                sendOTPEmail(email, token);
                            } catch (Exception e) {
                                throw new AuthenticationException("Cannot send email! Please try later!") {
                                };
                            }
                        },

                        // Create a new account for this email
                        () -> {
                            AppUser newUser = addUserV2(email);

                            // Generate new OTP
                            String token = OTPService.createRandomOneTimePassword();
                            tokenService.saveOneTimeLoginToken(newUser, token, Instant.now().plus(15, ChronoUnit.MINUTES));

                            try {
                                sendOTPEmail(email, token);
                            } catch (Exception e) {
                                throw new AuthenticationException("Cannot send email! Please try later!") {
                                };
                            }
                        }
                );
    }

    public void sendOTPEmail(String email, String token) throws MessagingException, UnsupportedEncodingException {
        String subject = "[TOEIC THINHSUY] Sign in to Toeic Thinhsuy";
        String senderName = "Toeic Thinhsuy";

        String mailContent =
        "<div style=\"max-width:500px; margin: auto; padding: 20px; font-family: Arial; font-size:24px; color: #211e4f; text-align:center\">\n" +
        "      <img src=\"https://i.imgur.com/BQxFZcI.png\" alt=\"Toeic Thinhsuy\" style=\"width:50%\">\n" +
        "      <p style=\"margin-bottom: 25px\">Welcome to Toeic Thinhsuy!</p>" +
        "       <p style=\"margin-bottom: 15px\"> Simply use this code to access your account. Your OTP code will expire in 15 minutes, and can only be used once.</p>\n" +
        "      <p style=\"color: #1560bd; font-weight:bold; font-size:48px\">" + token + "</p>\n" +
        " </div>\n";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("toeic-thinhsuy@tma.com.vn", senderName);
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        mailSender.send(message);
    }

    public AppUser verifyLoginToken(String token, EmailDTO emailDTO) {
        Token theToken = tokenService.getToken(token, TokenType.ONE_TIME_LOGIN);

        if (theToken.getExpireDate().isBefore(Instant.now())) {
            // Delete expired token
            tokenService.deleteToken(theToken);
            throw new AuthenticationException("Token expired") {};
        }

        AppUser user = theToken.getUser();

        if (!emailDTO.getEmail().equals(user.getEmail()))
            throw new AuthenticationException("Something went wrong! Please try again!") {};

        // Token is valid, delete it
        tokenService.deleteToken(theToken);
        return user;
    }

    public UserResponseDTO uploadAvatar(Long userId, MultipartFile avatar) throws NotFoundException {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id " + userId));

        user.setProfilePicture(filesStorageService.uploadFile(avatar, "/avatar"));

        return UserMapper.entityToUserResponseDto(
                userRepository.save(user)
        );
    }

    public UserResponseDTO updateProfile(Principal principal, UpdateProfileDTO updateProfileDTO) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        user.setFirstName(updateProfileDTO.getFirstName());
        user.setLastName(updateProfileDTO.getLastName());
        user.setPhone(updateProfileDTO.getPhone());

        return UserMapper.entityToUserResponseDto(userRepository.save(user));
    }

    public UserResponseDTO getProfile(Principal principal) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();
        return UserMapper.entityToUserResponseDto(userRepository.save(user));
    }

    public List<AttemptTestResultDTO> getAttemptedTest(Principal principal) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        return user.getAttemptTests()
                .stream().map(attemptTest -> attemptTestMapper.attemptTestToAttemptTestResultDTO(attemptTest))
                .toList();
    }

    public StreakResponseDTO getStreakStatus(Principal principal) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        return streakService.getStreakStatusToday(user);
    }

    public List<Badge> getBadges(Principal principal) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();
        return badgeRepository.findByUserId(user.getId());
    }

    public void updateGoals(Principal principal, UpdateGoalDTO updateGoalDTO) {
        AppUser user = ((SessionUser) ((Authentication) principal).getPrincipal()).getUserInfo();

        user.setVocabDailyGoals(updateGoalDTO.getVocabDailyGoal());
        user.setTestDailyGoals(updateGoalDTO.getTestDailyGoal());
        userRepository.save(user);
    }
}

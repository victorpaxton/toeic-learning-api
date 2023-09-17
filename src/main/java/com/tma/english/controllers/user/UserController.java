package com.tma.english.controllers.user;

import com.tma.english.exception.NotFoundException;
import com.tma.english.models.dto.UpdateGoalDTO;
import com.tma.english.models.dto.auth.UserResponseDTO;
import com.tma.english.models.dto.response.ErrorDTO;
import com.tma.english.models.dto.response.ResponseModel;
import com.tma.english.models.dto.user.UpdateProfileDTO;
import com.tma.english.services.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Users management")
public class UserController {

    @Autowired
    AppUserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieves all users successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    public ResponseModel<List<UserResponseDTO>, Void> getAllUsers() {
        return ResponseModel.<List<UserResponseDTO>, Void>builder()
                .isSuccess(true)
                .response(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Get a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieves all users successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class), mediaType = "application/json"))
    })
    public ResponseModel<UserResponseDTO, Void> getUser(@PathVariable Long id) throws NotFoundException {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseModel.<UserResponseDTO, Void>builder()
                .isSuccess(true)
                .response(user)
                .build();
    }

    @PostMapping(value = "/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update avatar for a user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel uploadAvatar(
            @PathVariable Long userId,
            @RequestPart MultipartFile image) throws NotFoundException {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userService.uploadAvatar(userId, image))
                .build();
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update my profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel updateProfile(Principal principal, @RequestBody @Valid UpdateProfileDTO updateProfileDTO) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userService.updateProfile(principal, updateProfileDTO))
                .build();
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get my profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel getProfile(Principal principal) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userService.getProfile(principal))
                .build();
    }

    @GetMapping("/attempted-tests")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get my attempted test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel getAttemptedTest(Principal principal) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userService.getAttemptedTest(principal))
                .build();
    }

    @GetMapping("/streak-status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get my streak status")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel getMyStreakStatus(Principal principal) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userService.getStreakStatus(principal))
                .build();
    }

    @GetMapping("/badges")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get my badges")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseModel getMyBadges(Principal principal) {
        return ResponseModel.builder()
                .isSuccess(true)
                .response(userService.getBadges(principal))
                .build();
    }

    @PutMapping("/goals")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update my goals")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity updateProfile(Principal principal, @RequestBody @Valid UpdateGoalDTO updateGoalDTO) {
        userService.updateGoals(principal, updateGoalDTO);
        return ResponseEntity.noContent().build();
    }
}

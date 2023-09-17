package com.tma.english.exception;

import com.tma.english.models.dto.response.ErrorDTO;
import com.tma.english.models.dto.response.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleAllException(Exception ex, WebRequest webRequest) {

        return ErrorDTO.builder()
                .code("500")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel<Void, ErrorDTO> handlerRequestException(NotFoundException ex) {
        return ResponseModel.<Void, ErrorDTO>builder()
                .isSuccess(false)
                .errors(ErrorDTO.builder()
                        .code("404")
                        .message(ex.getMessage())
                        .build())
                .build();
    }

    @ExceptionHandler({
            BadRequestException.class,
            BindException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            BadCredentialsException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handlerRequestException(Exception ex) {

        return ErrorDTO.builder()
                .code("400")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorDTO> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDTO errorDTO = new ErrorDTO(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });

        return errors;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO unauthorizedException(AuthenticationException ex) {
        return ErrorDTO.builder()
                .code("401")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO forbiddenException(AccessDeniedException ex) {
        return ErrorDTO.builder()
                .code("403")
                .message(ex.getMessage())
                .build();
    }
}

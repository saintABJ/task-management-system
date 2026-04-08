package com.example.tasks_mgt_sys.exceptions;


import com.example.tasks_mgt_sys.utils.ErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorHandler> handleInvalidCredentials(Exception ex){
        return new ResponseEntity<>(new ErrorHandler(ex.getMessage(), HttpStatus.NOT_FOUND.value(), System.currentTimeMillis()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorHandler> handleAccessDeniedExceptions(AccessDeniedException ex){
        return new ResponseEntity<>(new ErrorHandler(ex.getMessage(), HttpStatus.FORBIDDEN.value(), System.currentTimeMillis()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorHandler handleBadRequestException(CustomBadRequestException ex) {
        return new ErrorHandler(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorHandler handleEntityNotFoundException(ConflictException ex) {
        return new ErrorHandler(ex.getMessage(), HttpStatus.CONFLICT.value(), System.currentTimeMillis());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorHandler(ex.getMessage(), HttpStatus.NOT_FOUND.value(), System.currentTimeMillis());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorHandler handleMethodArgument(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return new ErrorHandler(errorMessage, HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorHandler handleInternalException(Exception ex) {

        System.out.println(ex.getMessage());
        return new ErrorHandler("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), System.currentTimeMillis());
    }

}

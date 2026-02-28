package com.ibrahim.restaurant.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReviewNotAllowedException.class)
    public ResponseEntity<ErrorDto> handleReviewNotAllowedException(ReviewNotAllowedException ex) {

        log.error("Caught ReviewNotAllowedException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("The specific review cannot be created or updated")
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String errorMessage = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorDto> handleStorageException(StorageException ex) {
        log.error("Caught StorageException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unable to save or retrieve at this time")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorDto> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {
        log.error("Caught RestaurantNotFoundException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("The specified restaurant wasn't found")
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorDto> handleReviewNotFoundException(ReviewNotFoundException ex) {
        log.error("Caught ReviewNotFoundException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("The specified review wasn't found")
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDto> handleBaseException(BaseException ex) {
        log.error("Caught BaseException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Caught unexpected exception", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

}

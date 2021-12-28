package com.mile.portal.config.exception;

import com.mile.portal.config.exception.exceptions.BadRequestException;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.config.exception.exceptions.TokenExpireException;
import com.mile.portal.config.exception.exceptions.message.ExceptionMessage;
import com.mile.portal.rest.common.model.comm.ResBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception exception, HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = createErrorResponse(null,
                ExceptionMessage.EXCEPTION_MESSAGE,
                httpServletRequest.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resbody);
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity bindException(BindException exception, HttpServletRequest httpServletRequest){
        List<Error> errorList = new ArrayList<>();

        BindingResult bindingResult = exception.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            String value = fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : "";

            log.error("fieldName: {} / message: {} / value: {}", fieldName, message, value);
            Error errorMessage = Error.builder()
                    .field(fieldName)
                    .message(message)
                    .invalidValue(value)
                    .build();

            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = createErrorResponse(errorList,
                ExceptionMessage.ARGUMENT_NOT_VALID_MESSAGE,
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resbody);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ResBody> badRequestException(BadRequestException exception, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = createErrorResponse(null,
                ExceptionMessage.BAD_REQUEST_MESSAGE,
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resbody);
    }

    @ExceptionHandler(value = {TokenExpireException.class})
    public ResponseEntity<ResBody> tokenExpireException(TokenExpireException exception, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = createErrorResponse(null,
                ExceptionMessage.TOKEN_EXPIRE_MESSAGE,
                httpServletRequest.getRequestURI(),
                HttpStatus.UNAUTHORIZED.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resbody);
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    public ResponseEntity<ResBody> EmptyResultDataAccessException(EmptyResultDataAccessException exception, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = createErrorResponse(null,
                ExceptionMessage.RESULT_NOT_FOUND_MESSAGE,
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resbody);
    }

    @ExceptionHandler(value = {ResultNotFoundException.class})
    public ResponseEntity<ResBody> resultNotFoundException(ResultNotFoundException exception, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = createErrorResponse(null,
                ExceptionMessage.RESULT_NOT_FOUND_MESSAGE,
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resbody);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ResBody> constraintViolationException(DataIntegrityViolationException exception, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = createErrorResponse(null,
                ExceptionMessage.CONSTRAINT_VIOLATION_MESSAGE,
                httpServletRequest.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resbody);
    }


    public ErrorResponse createErrorResponse(List<? extends Error> errorList, String message, String url, String statusCode){
        return ErrorResponse.builder()
                .errorList(errorList)
                .message(message)
                .requestUrl(url)
                .statusCode(statusCode)
                .build();
    }
}

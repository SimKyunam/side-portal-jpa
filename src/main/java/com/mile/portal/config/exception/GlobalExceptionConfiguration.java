package com.mile.portal.config.exception;

import com.mile.portal.config.exception.exceptions.BadRequestException;
import com.mile.portal.rest.common.model.dto.ResBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfiguration {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e,
                                    HttpServletRequest httpServletRequest){

        ErrorResponse errorResponse = createErrorResponse(null,
                "exception",
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, e.getMessage(), errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resbody);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                          HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            String value = fieldError.getRejectedValue().toString();

            log.error("fieldName: {} / message: {} / value: {}", fieldName, message, value);
            Error errorMessage = Error.builder()
                    .field(fieldName)
                    .message(message)
                    .invalidValue(value)
                    .build();

            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = createErrorResponse(errorList,
                "methodArgumentNotValidException",
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, e.getMessage(),errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resbody);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ResBody> badRequestExceptionHandler(HttpServletRequest httpServletRequest,
                                                              BadRequestException exception) {

        ErrorResponse errorResponse = createErrorResponse(null,
                "badRequestExceptionHandler",
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.toString()
        );

        ResBody resbody = new ResBody(ResBody.CODE_ERROR, exception.getMessage(),errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resbody);
    }


    public ErrorResponse createErrorResponse(List errorList, String message, String url, String statusCode){
        String requestUri = url;
        String[] deps = requestUri.split("/");
        String lastURL = deps[deps.length-1];

        return ErrorResponse.builder()
                .errorList(errorList)
                .message(message)
                .requestUrl(lastURL)
                .statusCode(statusCode)
                .build();
    }
}

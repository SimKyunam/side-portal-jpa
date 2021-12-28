package com.mile.portal.config.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    String statusCode;
    String requestUrl;
    String code;
    String message;
    String resultCode;

    List<?> errorList;
}

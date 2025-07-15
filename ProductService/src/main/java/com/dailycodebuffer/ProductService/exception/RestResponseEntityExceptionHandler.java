package com.dailycodebuffer.ProductService.exception;

import com.dailycodebuffer.ProductService.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceCustomException (ProductServiceCustomException exception) {
        new ErrorResponse();
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(exception.getMessage())
                .errorCode(exception.getErrorCode())
                .build(), HttpStatus.NOT_FOUND);
    }

}

package com.lemontree.wallet.wallet.ui.advice;

import com.lemontree.wallet.wallet.shared.base.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalCommonExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity onEntityNotFoundException(EntityNotFoundException exception) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .errorDetail(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity onRuntimeException(RuntimeException exception) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .errorDetail(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception exception) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .errorDetail("알 수 없는 에러 발생")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
    }
}

package com.lemontree.wallet.wallet.ui.advice;

import com.lemontree.wallet.wallet.shared.base.ErrorDetail;
import com.lemontree.wallet.wallet.shared.exception.BalanceLimitExceedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalCustomExceptionHandler {

    @ExceptionHandler(BalanceLimitExceedException.class)
    public ResponseEntity onApiRequestUnauthorizedException(BalanceLimitExceedException exception) {
        log.warn(exception.getMessage());
        ErrorDetail errorDetail = ErrorDetail.builder()
                .errorDetail(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }
}

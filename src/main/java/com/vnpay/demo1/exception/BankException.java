package com.vnpay.demo1.exception;

import com.vnpay.demo1.dto.BankResponse;
import com.vnpay.demo1.util.ResponseCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BankException {
    private static Logger log = LogManager.getLogger(BankException.class);
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BankResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Bank Request failed by miss parameter",ex);
        return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.DATA_REQUEST_ERROR, "Bank Request failed by miss parameter"));
    }
}

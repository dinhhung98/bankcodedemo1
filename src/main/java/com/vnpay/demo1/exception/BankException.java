package com.vnpay.demo1.exception;

import com.vnpay.demo1.dto.BankResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
public class BankException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BankResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Response error {}",new BankResponse("01", "Bank Request failed by miss parameter").toString());
        return ResponseEntity.badRequest().body(new BankResponse("01", "Bank Request failed by miss parameter"));
    }
}

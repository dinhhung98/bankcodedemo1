package com.vnpay.demo1.service;

import com.vnpay.demo1.dto.BankRequest;
import com.vnpay.demo1.dto.BankResponse;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface BankService {
    public ResponseEntity<BankResponse> saveAccount(BankRequest bankRequest) throws ParseException;
}

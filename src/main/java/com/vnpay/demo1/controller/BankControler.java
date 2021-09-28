package com.vnpay.demo1.controller;

import com.vnpay.demo1.dto.BankRequest;
import com.vnpay.demo1.dto.BankResponse;
import com.vnpay.demo1.service.BankServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.UUID;

@RestController
public class BankControler {
    @Autowired
    BankServiceImp bankService;
    private static Logger log = LogManager.getLogger(BankServiceImp.class);

    @PostMapping
    public ResponseEntity<BankResponse> getReponse(@Valid @RequestBody BankRequest bankRequest) throws ParseException {
        log.info("Bank request {}",bankRequest.toString());
        ThreadContext.put("token", UUID.randomUUID().toString());
        try {
            return bankService.saveAccount(bankRequest);
        }finally {
            ThreadContext.pop();
            ThreadContext.clearMap();
        }
    }
}

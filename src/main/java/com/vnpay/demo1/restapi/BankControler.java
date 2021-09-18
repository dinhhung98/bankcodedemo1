package com.vnpay.demo1.restapi;

import com.vnpay.demo1.dto.BankRequest;
import com.vnpay.demo1.dto.BankResponse;
import com.vnpay.demo1.module.BankCheck;
import com.vnpay.demo1.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
public class BankControler {

    @Autowired
    BankService bankService;

    @PostMapping
    public ResponseEntity<BankResponse> getReponse(@Valid @RequestBody BankRequest bankRequest) throws ParseException {
        BankCheck bankCheck = bankService.isBankCode(bankRequest.getBankCode());
        if (!bankCheck.isStatus()) {
            return ResponseEntity.badRequest().body(new BankResponse("02","Bank code not exist"));
        }
        if (!bankService.isPayDate(bankRequest.getPayDate())){
            return ResponseEntity.badRequest().body(new BankResponse("01","Pay Date not correct"));
        }
        boolean bankCheckSum = bankService.isCheckSum(bankRequest,bankCheck.getPrivateKey());
        if (!bankCheckSum) {
            return ResponseEntity.badRequest().body(new BankResponse("03","check sum not correct"));
        }
        if (!bankService.saveAccount(bankRequest.getBankCode(),bankRequest.getTokenKey(),bankRequest.toString())){
            return ResponseEntity.badRequest().body(new BankResponse("04","data can not save"));
        }
        return ResponseEntity.ok(new BankResponse("00","Success"));
    }
}

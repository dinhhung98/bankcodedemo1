package com.vnpay.demo1.controller;

import com.vnpay.demo1.dto.BankRequest;
import com.vnpay.demo1.dto.BankResponse;
import com.vnpay.demo1.model.BankCheck;
import com.vnpay.demo1.service.BankService;
import com.vnpay.demo1.util.ResponseCode;
import com.vnpay.demo1.util.ResponseMessage;
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
            return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.BANK_CODE_ERROR, ResponseMessage.MSG_BANK_CODE_ERROR));
        }
        if (!bankService.isPayDate(bankRequest.getPayDate())) {
            return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.DATA_REQUEST_ERROR, ResponseMessage.MSG_PAY_DATE_ERROR));
        }
        boolean bankCheckSum = bankService.isCheckSum(bankRequest, bankCheck.getPrivateKey());
        if (!bankCheckSum) {
            return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.CHECK_SUM_ERROR, ResponseMessage.MSG_CHECK_SUM_ERROR));
        }
        if (!bankService.saveAccount(bankRequest.getBankCode(), bankRequest.getTokenKey(), bankRequest.toString())) {
            return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.SAVE_ERROR, ResponseMessage.MSG_SAVE_ERROR));
        }
        return ResponseEntity.ok(new BankResponse(ResponseCode.CODE_SUCCESS, ResponseMessage.MSG_SUCCESS));
    }
}

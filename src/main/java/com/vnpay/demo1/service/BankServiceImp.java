package com.vnpay.demo1.service;

import com.google.common.hash.Hashing;
import com.vnpay.demo1.config.ConfigBankYaml;
import com.vnpay.demo1.dto.BankRequest;
import com.vnpay.demo1.dto.BankResponse;
import com.vnpay.demo1.model.Bank;
import com.vnpay.demo1.util.ResponseCode;
import com.vnpay.demo1.util.ResponseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Service
public class BankServiceImp implements BankService{
    @Autowired
    private ConfigBankYaml bankYaml;
    @Autowired
    private JedisPool redisPool;

    private static Logger log = LogManager.getLogger(BankServiceImp.class);

    private String findBankByBankCode(String bankCode) {
        log.info("Begin find privateKey with bank code-{}", bankCode);
        ArrayList<Bank> banks = (ArrayList<Bank>) bankYaml.getListBank();
        for (Bank bank : banks) {
            if (bank.getBankCode().equals(bankCode)) {
                return bank.getPrivateKey();
            }
        }
        return "";
    }

    private boolean isCheckSum(BankRequest bankRequest, String privateKey) {
        String tempCheck = bankRequest.getMobile() + bankRequest.getBankCode() + bankRequest.getAccountNo()
                + bankRequest.getPayDate() + bankRequest.getDebitAmount() + bankRequest.getRespCode()
                + bankRequest.getTraceTransfer() + bankRequest.getMessageType() + privateKey;
        log.info("Data before check sum {}",tempCheck);
        String sha256 = Hashing.sha256().hashString(tempCheck, StandardCharsets.UTF_8).toString();
        log.info("Data after check sum {}",sha256);
        if (bankRequest.getCheckSum().equals(sha256)) {
            return true;
        }
        return false;
    }

    public ResponseEntity<BankResponse> saveAccount(BankRequest bankRequest) throws ParseException {
        log.info("Bank request: bankCode {}, TokenKey-{}, Data-{}",bankRequest.getBankCode(),bankRequest.getTokenKey(),bankRequest.toString());
        Jedis jedis = redisPool.getResource();
        String privateKey = findBankByBankCode(bankRequest.getBankCode());
        if (Strings.isEmpty(privateKey)){
            return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.BANK_CODE_ERROR, ResponseMessage.MSG_BANK_CODE_ERROR));
        }
        if (!isCheckSum(bankRequest,privateKey)){
            return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.CHECK_SUM_ERROR, ResponseMessage.MSG_CHECK_SUM_ERROR));
        }
        if (!isPayDate(bankRequest.getPayDate())){
            return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.DATA_REQUEST_ERROR, ResponseMessage.MSG_PAY_DATE_ERROR));
        }
        try {
            jedis.hset(bankRequest.getBankCode(), bankRequest.getTokenKey(), bankRequest.toString());
            return ResponseEntity.ok(new BankResponse(ResponseCode.CODE_SUCCESS, ResponseMessage.MSG_SUCCESS));
        } catch (Exception e) {
            log.error("Save bank to redis exception ",e);
        } finally {
            jedis.close();
        }
        return ResponseEntity.badRequest().body(new BankResponse(ResponseCode.SAVE_ERROR,ResponseMessage.MSG_SAVE_ERROR));
    }

    private boolean isPayDate(String payDate) throws ParseException {
        log.info("Pay Date: {}",new SimpleDateFormat("yyyyMMddhhmmss").parse(payDate).toString());
        if (payDate.matches("^[0-9]{14}$")){
            return true;
        }
        return false;
    }
}

package com.vnpay.demo1.service;

import com.google.common.hash.Hashing;
import com.vnpay.demo1.config.ConfigBankYaml;
import com.vnpay.demo1.dto.BankRequest;
import com.vnpay.demo1.module.Bank;
import com.vnpay.demo1.module.BankCheck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Service
public class BankService {
    @Autowired
    private ConfigBankYaml bankYaml;
    @Autowired
    private JedisPool bankRedis;

    private static Logger log = LogManager.getLogger(BankService.class);

    public BankCheck isBankCode(String bankCode) {
        log.info("bank code-{}", bankCode);
        ArrayList<Bank> banks = (ArrayList<Bank>) bankYaml.getListBank();
        for (Bank bank : banks) {
            if (bank.getBankCode().equals(bankCode)) {
                return new BankCheck(true, bankCode);
            }
        }
        return new BankCheck(false, bankCode);
    }

    public boolean isCheckSum(BankRequest bankRequest, String privateKey) {
        String tempCheck = bankRequest.getMobile() + bankRequest.getBankCode() + bankRequest.getAccountNo()
                + bankRequest.getPayDate() + bankRequest.getDebitAmount() + bankRequest.getRespCode()
                + bankRequest.getTraceTransfer() + bankRequest.getMessageType() + privateKey;

        String sha256 = Hashing.sha256()
                .hashString(tempCheck, StandardCharsets.UTF_8).toString();

        if (bankRequest.getCheckSum().equals(sha256)) {
            return true;
        }
        return false;
    }

    public boolean saveAccount(String bankCode, String tokenKey, String data) {
        log.info("Bank Request: bankCode {}, TokenKey-{}, Data-{}",bankCode,tokenKey,data);
        Jedis jedis = bankRedis.getResource();
        try {
            jedis.hset(bankCode, tokenKey, data);
            return true;
        } catch (Exception e) {
            log.info("save fail {}",e);
        } finally {
            jedis.close();
        }
        return false;
    }

    public boolean isPayDate(String payDate) throws ParseException {
        log.info("Pay Date: {}",new SimpleDateFormat("yyyyMMddhhmmss").parse(payDate).toString());
        if (payDate.matches("^[0-9]{14}$")){
            return true;
        }
        return false;
    }
}

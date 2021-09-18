package com.vnpay.demo1.module;

import lombok.Data;

@Data
public class BankCheck {
    private boolean status;
    private String privateKey;

    public BankCheck() {
    }

    public BankCheck(boolean status, String privateKey) {
        this.status = status;
        this.privateKey = privateKey;
    }

    public boolean isStatus() {
        return status;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}

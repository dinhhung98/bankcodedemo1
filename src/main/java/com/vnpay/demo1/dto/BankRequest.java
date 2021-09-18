package com.vnpay.demo1.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Data
public class BankRequest {
    @NotBlank
    private String tokenKey;
    @NotBlank
    private String apiID;
    @NotBlank
    private String mobile;
    @NotBlank
    private String bankCode;
    @NotBlank
    private String accountNo;
    @NotBlank
    private String payDate;
    @NotBlank
    private String additionalData;
    @NotBlank
    private String debitAmount;
    @NotBlank
    private String respCode;
    @NotBlank
    private String respDesc;
    @NotBlank
    private String traceTransfer;
    @NotBlank
    private String messageType;
    @NotBlank
    private String checkSum;
    @NotBlank
    private String orderCode;
    @NotBlank
    private String userName;
    @NotBlank
    private String realAmount;
    @NotBlank
    private String promotionCode;
}

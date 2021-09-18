package com.vnpay.demo1.config;

import com.vnpay.demo1.module.Bank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "banks")
@PropertySource(value =  "classpath:banks.yml",factory = YamlPropertySourceFactory.class)
@Data
public class ConfigBankYaml {
    private List<Bank> listBank;
}

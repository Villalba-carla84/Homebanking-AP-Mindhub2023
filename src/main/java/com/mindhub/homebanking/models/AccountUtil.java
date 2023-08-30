package com.mindhub.homebanking.models;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountUtil {
    private static final String ACCOUNT_PREFIX = "VIN";

    public String generateAccountNumber() {
        Random random = new Random();
        int accountSuffix = random.nextInt(99999999); // Máximo 8 dígitos
        return ACCOUNT_PREFIX + "-" + accountSuffix;
    }
}

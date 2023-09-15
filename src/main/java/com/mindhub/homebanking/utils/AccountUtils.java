package com.mindhub.homebanking.utils;

import java.util.Random;

public final class AccountUtils {

    public AccountUtils() {
    }

    private static final String ACCOUNT_PREFIX = "VIN";

    public static String generateAccountNumber() {
        Random random = new Random();
        int accountSuffix = random.nextInt(99999999); // Máximo 8 dígitos
        return ACCOUNT_PREFIX + "-" + accountSuffix;
    }
}

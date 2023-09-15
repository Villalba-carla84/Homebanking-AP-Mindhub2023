package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {

    private CardUtils() {
    }
    public static String getCardNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            number.append(random.nextInt(10000));
            if (i < 3) {
                number.append("-");
            }

        } return number.toString();
    }

    public static int  getCVV(){
        Random randomCvv = new Random();
        return randomCvv.nextInt(900) + 100;
    }

}

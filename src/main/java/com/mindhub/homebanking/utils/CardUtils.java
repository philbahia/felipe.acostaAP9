package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public final class CardUtils {

    @Autowired
    private static CardService cardService;



    public static int generateCvv() {
        Random random = new Random();
        return random.nextInt(900) + 100;
    }

    public static String generateCardNumber(){
        String cardNumber;
        // TODO: 31/8/2023 verificar si existe nro en la base

        do {
            cardNumber = randomCardNumber();
        } while (cardService.existsByNumber(cardNumber));

        return cardNumber;
    }

    public static String randomCardNumber() {
        Random random = new Random();
        StringBuilder numberCard = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            numberCard.append(digit);

            if (i % 4 == 3 && i != 15) {
                numberCard.append(" ");
            }
        }

        return numberCard.toString();
    }
}

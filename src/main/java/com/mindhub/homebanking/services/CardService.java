package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {

    void addCard(Card card);

    Boolean existsByNumber(String cardNumber);

}

package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;


    @Autowired
    private CardRepository cardRepository;


    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> getCurrentClientCardss(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client != null) {
            List<CardDTO> cardDTOs = client.getCards().stream()
                    .map(CardDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(cardDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor
    ) {

        // Consulto x el cliente con sesión iniciada
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No valid client");
        }

        long existingCardCount = client.getCards()
                .stream()
                .filter(card -> card.getType().equals(cardType))
                .count();


        if (existingCardCount >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Client already has 3 cards of the same type");
        }

        boolean sameCardColor = client.getCards()
                .stream()
                .anyMatch(card -> card.getType().equals(cardType) && card.getColor().equals(cardColor));

        if (sameCardColor) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Client already has a card of the same color for this type");
        }



        String numberCard = generateCardNumber();
        int cvv = generateCvv();

        Card newCard = new Card(client.toString(),
                            cardType,
                            cardColor,
                            numberCard,
                            cvv,
                            LocalDate.now(),
                            LocalDate.now().plusYears(5));

        client.addCard(newCard);
        cardRepository.save(newCard);

        return ResponseEntity.status(HttpStatus.CREATED).body("Account Created");

    }




    public  int generateCvv() {
        Random random = new Random();
        return random.nextInt(900) + 100;
    }

    public  String generateCardNumber(){
        String cardNumber;
        Set<String> existCards = new HashSet<>();

        do {
            cardNumber = randomCardNumber();
        } while (existCards.contains(cardNumber));

        return cardNumber;
    }

    public  String randomCardNumber() {
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

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(path="/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                              @RequestParam CardType cardType,
                                              Authentication authentication)
        {
            String userEmail = authentication.getName();
            Client client = clientRepository.findByEmail(userEmail);

            if (client != null) {
                // Obtener tarjetas del cliente del tipo y contar cuántas ya tiene
                long existingCardsCount = client.getCardsClient().stream()//para obtener una secuencia de las tarjetas que tiene el cliente
                        .filter(card -> card.getType() == cardType)// para filtrar solo las tarjetas que coinciden con el tipo de tarjeta especificado
                        .count(); //para contar cuántas tarjetas del mismo tipo ya tiene

              /*  List<Card> cardsFiltered = client.getCardsClient().stream().filter(card -> card.getType() == cardType).collect(Collectors.toList());
                if (existingCardsCount >= 3) {
                   return new ResponseEntity<>("Already have 3 cards of this type", HttpStatus.FORBIDDEN);
                }*/
                // Verificar si el cliente ya tiene una tarjeta del mismo color
              boolean hasCardWithSameColor = client.getCardsClient().stream()
                        .anyMatch(card -> card.getColor() == cardColor);

               if (hasCardWithSameColor) {
                   return new ResponseEntity<>("Already have a card with the same color", HttpStatus.FORBIDDEN);
               }
                else {
                    Card card = new Card();
                    card.setColor(cardColor);
                    card.setType(cardType);
                    card.setCardholder(client.getFirstName() + " " + client.getLastName());
                    card.setNumber(generateCardNumber());
                    card.setCvv(generateRandomCvv());
                    card.setFromDate(LocalDate.now());
                    card.setThruDate(LocalDate.now().plusYears(5));

                    client.addCard(card);
                    cardRepository.save(card);
                    clientRepository.save(client);

                    return new ResponseEntity<>("Card created", HttpStatus.CREATED);
                }
            } else {
                throw new UsernameNotFoundException("Unknown user: " + userEmail);
            }
        }

    // Métodos para generar números de tarjeta y CVV
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            cardNumber.append(random.nextInt(10000));
            if (i < 3) {
                cardNumber.append("-");
            }
        }

        return cardNumber.toString();
    }

    private int generateRandomCvv() {
        Random random = new Random();
        return random.nextInt(900) + 100;
    }








}

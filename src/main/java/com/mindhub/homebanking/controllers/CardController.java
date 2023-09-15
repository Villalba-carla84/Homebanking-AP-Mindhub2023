package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;

import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
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
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.CardUtils.getCVV;
import static com.mindhub.homebanking.utils.CardUtils.getCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;



    @GetMapping("/cards")
    public Set<CardDTO> cards(){
        return cardService.getCards();
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                              @RequestParam CardType cardType,
                                              Authentication authentication)
        {

            Client client = clientService.getClientByEmail(authentication.getName());

            if (client != null) {
                // Obtener tarjetas del cliente del tipo y contar cuántas ya tiene
                long existingCardsCount = client.getCardsClient().stream()//para obtener una secuencia de las tarjetas que tiene el cliente
                        .filter(card -> card.getType() == cardType)// para filtrar solo las tarjetas que coinciden con el tipo de tarjeta especificado
                        .count(); //para contar cuántas tarjetas del mismo tipo ya tiene

               List<Card> cardsFiltered = client.getCardsClient().stream().filter(card -> card.getType() == cardType).collect(Collectors.toList());
                if (existingCardsCount >= 3) {
                   return new ResponseEntity<>("Already have 3 cards of this type", HttpStatus.FORBIDDEN);
                }
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
                    card.setNumber(getCardNumber());
                    card.setCvv(getCVV());
                    card.setFromDate(LocalDate.now());
                    card.setThruDate(LocalDate.now().plusYears(5));

                    client.addCard(card);
                    cardService.saveCards(card);
                    clientService.saveClient(client);

                    return new ResponseEntity<>("Card created", HttpStatus.CREATED);
                }
            } else {
                throw new UsernameNotFoundException("Unknown user: " + authentication.getName());
            }


        }





}

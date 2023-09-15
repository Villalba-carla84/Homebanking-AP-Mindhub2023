package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Set<CardDTO> getCards(){
         return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toSet());
    }
    @Override
   public void saveCards (Card card){
       cardRepository.save(card);
   }





}

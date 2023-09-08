package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {
    @Autowired
    private CardRepository cardRepository;



}

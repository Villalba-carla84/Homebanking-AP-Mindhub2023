package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;

import com.mindhub.homebanking.models.Client;


import java.util.List;

public interface ClientService {


    Client getClientByEmail (String email);

    void saveClient(Client client);

    List<ClientDTO> getClientsDTO();

    Client getClientById( Long id);

}









package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;


    @Override
    public Client getClientByEmail (String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client){

        clientRepository.save(client);
    }

    @Override
    public List<ClientDTO> getClientsDTO(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @Override
     public Client getClientById( Long id){
         return clientRepository.findById(id).orElse(null);
     }

    @Override
    public Client getClientCurrent(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName());
    }




}

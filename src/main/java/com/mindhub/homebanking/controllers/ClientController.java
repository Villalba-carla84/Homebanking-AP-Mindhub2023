package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.utils.AccountUtils.generateAccountNumber;


@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @GetMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication){
        return new ClientDTO(clientService.getClientCurrent(authentication));
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {

        return clientService.getClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){

        return new ClientDTO(clientService.getClientById(id));


    }
    @PostMapping("/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

// Crear una cuenta y asociarla al cliente
        String accountNumber =generateAccountNumber();
        Account account = new Account(accountNumber, LocalDate.now(), 0);
        newClient.addAccount(account);
        account.setClient(newClient);

        // Guardar el cliente y la cuenta en el repositorio
        clientService.saveClient(newClient);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

 }



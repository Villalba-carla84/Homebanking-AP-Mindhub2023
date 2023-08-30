package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountUtil;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountUtil accountUtil;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts () {

        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){

        AccountDTO account= new AccountDTO(Objects.requireNonNull(accountRepository.findById(id).orElse(null)));
        return account;


    }



    private static final String ACCOUNT_PREFIX = "VIN";
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> createAccount( Authentication authentication) {

        Client client= clientRepository.findByEmail(authentication.getName());
        if (client==null){
            return new ResponseEntity<>("user not found ", HttpStatus.FORBIDDEN);
        }

        // Verificar si el cliente ya tiene 3
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Maximum account limit reached", HttpStatus.FORBIDDEN);
        }
        // Generar un número de cuenta aleatorio
        String accountNumber = accountUtil.generateAccountNumber();
        // Crear la cuenta
        Account account = new Account(accountNumber, LocalDate.now() , 0);

        // Guardar la cuenta
        client.addAccount(account);
        account.setClient(client);
        accountRepository.save(account);


        // Crear un objeto que contenga los detalles de la cuenta
        AccountDTO accountDTO = new AccountDTO(account);

        // Devolver los detalles de la cuenta en la respuesta
        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);

    }



}

package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.utils.AccountUtils.generateAccountNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts () {
        return accountService.getListAccountsDTO();

    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){

      return accountService.getAccountById(id);
    }



    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());

        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }


    @PostMapping("/clients/current/accounts")

    public ResponseEntity<Object> createAccount( Authentication authentication) {

        Client client= clientService.getClientByEmail(authentication.getName());

        // Verificar si el cliente ya tiene 3
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Maximum account limit reached", HttpStatus.FORBIDDEN);
        }
        // Generar un número de cuenta aleatorio
        String accountNumber = generateAccountNumber();
        // Crear la cuenta
        Account account = new Account(accountNumber, LocalDate.now() , 0);

        // Guardar la cuenta
        client.addAccount(account);
        account.setClient(client);
        accountService.saveAccount(account);



        // Crear un objeto que contenga los detalles de la cuenta
        AccountDTO accountDTO = new AccountDTO(account);

        // Devolver los detalles de la cuenta en la respuesta
        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);

    }



}

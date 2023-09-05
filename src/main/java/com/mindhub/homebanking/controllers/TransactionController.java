package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @RequestMapping(path="/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object>createTransaction(
            @RequestParam double amount,
            @RequestParam String description,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            Authentication authentication){

        if(Double.isNaN(amount)|| description.isEmpty()|| fromAccountNumber.isEmpty()|| toAccountNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.BAD_REQUEST);
        }//verifica parametros vacios

        if(fromAccountNumber.equals(toAccountNumber)){
          return new ResponseEntity<>("Source and Target accounts cannot be the same",HttpStatus.BAD_REQUEST);
        }//verifica que los numeros de cuenta no sean iguales

        // Obtener cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());

        Account sourseAccount = accountRepository.findByNumber(fromAccountNumber);
        Account targetAccount = accountRepository.findByNumber(toAccountNumber); //busca cuenta de origen y destino

        if(sourseAccount == null){
            return new ResponseEntity<>("Account/s not found", HttpStatus.FORBIDDEN);
        }

        Client authenticatedClient = sourseAccount.getClient();
        String authenticadedUserEmail = authentication.getName();
        if(!authenticatedClient.getEmail().equals(authenticadedUserEmail)){
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
            //verifica que la cuenta de origen pertenezca al cliente logeado
        }

        if(sourseAccount.getBalance()< amount){
            return new ResponseEntity<>("Insufficient balance",HttpStatus.BAD_REQUEST);
            //verifica que la cuenta de origen tenga monto disponible
        }


        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amount,description + " DEBIT - " + fromAccountNumber, LocalDateTime.now(),sourseAccount);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,amount,description + " CREDIT - " + toAccountNumber, LocalDateTime.now(),targetAccount);
        //crea transacciones



        sourseAccount.setBalance(sourseAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        //actualiza saldos de las cuentas

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);//guarda transacciones a traves del repositorio
        accountRepository.save(sourseAccount);
        accountRepository.save(targetAccount);//guarda cuentas a traves del  repositorio


        return new ResponseEntity<>("Transaction created",HttpStatus.CREATED);
    }

}

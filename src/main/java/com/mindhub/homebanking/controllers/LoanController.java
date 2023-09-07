package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    TransactionRepository transactionRepository;
@RequestMapping(path= "/loans",method = RequestMethod.GET)
public List<LoanDTO> getLoanApplicationDTO() {
    return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
                          //servicio GET de “/api/loans” debes crear un LoanDTO que puedas usar para retornar los préstamos disponibles
}


    @Transactional
    @RequestMapping(path ="/loans",method= RequestMethod.POST)
    public ResponseEntity<Object> applyforloan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        //Debe recibir un objeto de solicitud de crédito con los datos del préstamo

        //Verificar que los datos sean correctos, es decir no estén vacíos,que el monto no sea 0 oque las cuotas no sean 0.
        if (loanApplicationDTO.getLoanId() < 1 || loanApplicationDTO.getAmount()<=0 || loanApplicationDTO.getPayments()<=0){
            return new ResponseEntity<>("Empty data",HttpStatus.FORBIDDEN);
        }

        //Verificar que el préstamo exista
      Loan loan = loanRepository.getLoanById(loanApplicationDTO.getLoanId());
        if (loan == null) {
            return new ResponseEntity<>("the Loan not exist", HttpStatus.FORBIDDEN);
        }

        //Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if (loan.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("the amount exceeds the allowable", HttpStatus.FORBIDDEN);
        }

        //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Number of payments not available", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino exista
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        if (account == null) {
            return new ResponseEntity<>("The account destiny not exist", HttpStatus.FORBIDDEN);
        }
        //verificar que la cuenta de destino pertenezca a un cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Destination account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }

        //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        double loanInterest = (loanApplicationDTO.getAmount() * 0.20) + loanApplicationDTO.getPayments();


        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved", LocalDateTime.now(), account);
        account.addTransaction(transaction);
        transactionRepository.save(transaction);

        //actualizo los montos
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(account);


       return new ResponseEntity<>(HttpStatus.CREATED);

    }}


































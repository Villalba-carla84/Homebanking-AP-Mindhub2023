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

@RequestMapping(path= "/api/loan",method = RequestMethod.GET)
public List<LoanDTO> getLoanApplicationDTO() {
    return loanRepository.findAll().stream().map(loan->new LoanDTO(loan)).collect(Collectors.toList());
//servicio GET de “/api/loans” debes crear un LoanDTO que puedas usar para retornar los préstamos disponibles
}

    @Transactional
    @RequestMapping(path= "/clients/current/loans",method= RequestMethod.POST)
    public ResponseEntity<Object> applyforloan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
//Debe recibir un objeto de solicitud de crédito con los datos del préstamo


        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findByName(loanApplicationDTO.getLoanId());
        Account account = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccountNumber());


        Account accountOrigin = accountRepository.findByNumber(account.getNumber());
        Account accountDestiny = accountRepository.findByNumber(account.getNumber());

        if(loanApplicationDTO==null){
            return new ResponseEntity<>("data not fount", HttpStatus.FORBIDDEN);
        }

//Verificar que los datos sean correctos, es decir no estén vacíos
       if(loanApplicationDTO.getAmount()==0 || loanApplicationDTO.getPayments()==0){
           return new ResponseEntity<>("Empty data",HttpStatus.FORBIDDEN);
       }

//Verificar que el préstamo exista
        if(loan == null){
            return new ResponseEntity<>("The Loan does not exist",HttpStatus.FORBIDDEN);
        }

//Verificar que el monto solicitado no exceda el monto máximo del préstamo

        if(loanApplicationDTO.getAmount()>loan.getMaxAmount() || loanApplicationDTO.getAmount() <=0){
            return new ResponseEntity<>("Exceeds the maximun amount available or the amount is negative",HttpStatus.FORBIDDEN);
        }

//Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Number of payments not available", HttpStatus.FORBIDDEN);

        }
//Verificar que la cuenta de destino exista
        if(accountDestiny==null){
            return new ResponseEntity<>("The account destiny not exist",HttpStatus.FORBIDDEN);

        }

//Verificar que la cuenta de destino pertenezca al cliente autenticado
        if(!accountDestiny.getClient().equals(client)){
            return new ResponseEntity<>("Destination account does not belong to the authenticated client",HttpStatus.FORBIDDEN);
        }

//Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        double totalLoanAmount = loanApplicationDTO.getAmount() * 1.2;

//crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo)
// con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(),loanApplicationDTO.getPayments(),client,loan);
        Transaction transactionLoan = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(), loan.getName() + "loan approved ", LocalDateTime.now(),account);

        transactionRepository.save(transactionLoan);
        clientLoanRepository.save(clientLoan);

//descuento de las cuentas
        Double origin = loanApplicationDTO.getAmount()-account.getBalance();
        Double destiny =loanApplicationDTO.getAmount()+ account.getBalance();

////actualizo los montos
        accountOrigin.setBalance((origin));
        accountDestiny.setBalance(destiny);

        return new ResponseEntity<>("The loan is applied",HttpStatus.CREATED);

    }


































}

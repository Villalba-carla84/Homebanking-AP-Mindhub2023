package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){

		return (args -> {

			Client client1 = new Client("Melba","Morel","melba@mindhub.com");
			Client client2 = new Client("Felipe","Ulloa","felipe000@mindhub.com");
			Client client3 = new Client("Martina","blas","martu@mindhub.com");
			Client client4 = new Client("Susana","Festa","sufesta@mindhub.com");

			clientRepository.save(client1);
		    clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);


			Account account1 = new Account("VIN001" , LocalDate.now() , 5000.0);
     		Account account2 = new Account("VIN002", LocalDate.now().plus(Period.ofDays(1)),7500.0);
			Account account3 = new Account("VIN003" , LocalDate.now() , 1000.0);
			Account account4 = new Account("VIN004", LocalDate.now().plus(Period.ofDays(6)),710500.0);
			Account account5 = new Account("VIN005" , LocalDate.now() , 3500.0);
			Account account6 = new Account("VIN006", LocalDate.now().plus(Period.ofDays(20)),500.0);


			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client3.addAccount(account4);
			client3.addAccount(account5);
			client4.addAccount(account6);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);
			accountRepository.save(account6);


			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 7000.0, "transfer received", LocalDateTime.now() );
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 15000, "transfer received", LocalDateTime.now() );
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -9500, "Buys xx", LocalDateTime.now() );
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -2750, "Buys x", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 7000, "transfer received", LocalDateTime.now());

			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account1.addTransaction(transaction5);


			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);


			Loan loan1 = new Loan("Hipotecario", 500000.0, List.of(12,24,36,48,60));
			Loan loan2 = new Loan ( "Personal", 100000.0 , List.of(6,12,24));
			Loan loan3 = new Loan ( "Automotriz", 300000.0, List.of(6,12,24,36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 60);

			clientLoan1.setClient(client1);
            clientLoan1.setLoan(loan1);

			clientLoan2.setClient(client1);
			clientLoan2.setLoan(loan2);


			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);


			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36);


			clientLoan3.setClient(client2);
			clientLoan3.setLoan(loan2);

			clientLoan4.setClient(client2);
			clientLoan4.setLoan(loan3);

            clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);



		});
	}
}

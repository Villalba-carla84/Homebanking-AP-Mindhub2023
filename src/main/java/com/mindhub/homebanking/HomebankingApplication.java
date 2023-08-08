package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDate;
import java.time.Period;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){

		return (args -> {

			Client client1 = new Client("Melba","Morel","melba@mindhub.com");
			/*Client client2 = new Client("Felipe","Ulloa","felipe000@mindhub.com");
			Client client3 = new Client("Martina","blas","martu@mindhub.com");
			Client client4 = new Client("Susana","Festa","sufesta@mindhub.com");*/

			clientRepository.save(client1);
			/*clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);*/


			Account account1 = new Account("VIN001" , LocalDate.now() , 5000.0);


			Account account2 = new Account("VIN002", LocalDate.now().plus(Period.ofDays(1)),7500.0);

			client1.addAccount(account1);
			client1.addAccount(account2);
            accountRepository.save(account1);

			accountRepository.save(account2);

		});
	}
}

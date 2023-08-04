package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){

		return (args -> {

			Client client1 = clientRepository.save(new Client("Melba","Morel","melba@mindhub.com"));
			Client client2 = clientRepository.save(new Client("Felipe","Ulloa","felipe000@mindhub.com"));
			Client client3 = clientRepository.save(new Client("Martina","blas","martu@mindhub.com"));
			Client client4 = clientRepository.save(new Client("Susana","Festa","sufesta@mindhub.com"));
		});
	}
}

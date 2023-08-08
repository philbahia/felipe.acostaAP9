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

@SpringBootApplication
public class HomebankingApplication {


    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);

    }
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args) -> {

			Client client = new Client("Melba","Morel","morel@gmail.com");
			clientRepository.save(client);

            Account account = new Account("VIN001", LocalDate.now(),5000);
            client.addAccount(account);
            accountRepository.save(account);

            Account account1 = new Account("VIN002", LocalDate.now().plusDays(1),7500);
            client.addAccount(account1);
            accountRepository.save(account1);


            Client client1 = new Client("Briana","Brichu","brichu@gmail.com");
            clientRepository.save(client1);

            Account account2 = new Account("VIN012", LocalDate.now().plusDays(1),71500);
            client1.addAccount(account2);
            accountRepository.save(account2);


        };
	}

}

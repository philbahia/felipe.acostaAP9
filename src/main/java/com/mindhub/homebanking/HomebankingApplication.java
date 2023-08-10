package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;




@SpringBootApplication
public class HomebankingApplication {


    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);

    }
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository){
		return (args) -> {

			Client client = new Client("Melba","Morel","morel@gmail.com");
			clientRepository.save(client);

            Account account = new Account("VIN001", LocalDate.now(),5000);
            client.addAccount(account);
            accountRepository.save(account);

            Transaction transaction = new Transaction(TransactionType.DEBIT,-15000,"School Payment",LocalDate.now());
            account.addTransaction(transaction);
            transactionRepository.save(transaction);

            Transaction transaction1 = new Transaction(TransactionType.CREDIT,225000,"Loan",LocalDate.now());
            account.addTransaction(transaction1);
            transactionRepository.save(transaction1);

            Account account1 = new Account("VIN002", LocalDate.now().plusDays(1),7500);
            client.addAccount(account1);
            accountRepository.save(account1);

            Transaction transaction2 = new Transaction(TransactionType.DEBIT,-150000,"Debt",LocalDate.now());
            account1.addTransaction(transaction2);
            transactionRepository.save(transaction2);

            Transaction transaction3 = new Transaction(TransactionType.CREDIT,2225000,"Revenue",LocalDate.now());
            account1.addTransaction(transaction3);
            transactionRepository.save(transaction3);


            Client client1 = new Client("Briana","Brichu","brichu@gmail.com");
            clientRepository.save(client1);

            Account account2 = new Account("VIN012", LocalDate.now().plusDays(1),71500);
            client1.addAccount(account2);
            accountRepository.save(account2);

                List<Integer> payments =Arrays.asList (60,40,24,12);
                List<Integer> payments2 = Arrays.asList(48,24,12);
                List<Integer> payments3 = Arrays.asList(80,60,48,24,12);
                Loan loan1= new Loan("hipotecario",500000.0,payments);
                Loan loan2= new Loan("personal", 200000.0,payments2);
                Loan loan3= new Loan("automotriz", 100000.0,payments3);
                loanRepository.save(loan1);
                loanRepository.save(loan2);
                loanRepository.save(loan3);


        };
	}

}

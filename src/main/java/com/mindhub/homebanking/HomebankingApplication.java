package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;




@SpringBootApplication
public class HomebankingApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);

    }


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository) {
        return (args) -> {


            Client client = new Client("Melba", "Morel", "morel@gmail.com", passwordEncoder.encode("coquito"));
            clientRepository.save(client);

            Account account = new Account("VIN001", LocalDate.now(), 5000);
            client.addAccount(account);
            accountRepository.save(account);

            Transaction transaction = new Transaction(TransactionType.DEBIT, -15000, "School Payment", LocalDateTime.now());
            account.addTransaction(transaction);
            transactionRepository.save(transaction);

            Transaction transaction1 = new Transaction(TransactionType.CREDIT, 225000, "Loan", LocalDateTime.now());
            account.addTransaction(transaction1);
            transactionRepository.save(transaction1);

            Account account1 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
            client.addAccount(account1);
            accountRepository.save(account1);

            Transaction transaction2 = new Transaction(TransactionType.DEBIT, -150000, "Debt", LocalDateTime.now());
            account1.addTransaction(transaction2);
            transactionRepository.save(transaction2);

            Transaction transaction3 = new Transaction(TransactionType.CREDIT, 2225000, "Revenue", LocalDateTime.now());
            account1.addTransaction(transaction3);
            transactionRepository.save(transaction3);


            Client client1 = new Client("Briana", "Brichu", "brichu@gmail.com", passwordEncoder.encode("brichus"));
            clientRepository.save(client1);

            Account account2 = new Account("VIN012", LocalDate.now().plusDays(1), 71500);
            client1.addAccount(account2);
            accountRepository.save(account2);

            List<Integer> payments = Arrays.asList(12, 24, 36, 48, 60);
            List<Integer> payments2 = Arrays.asList(6, 12, 24);
            List<Integer> payments3 = Arrays.asList(6, 12, 24, 36);
            Loan loan1 = new Loan("hipotecario", 500000.0, payments);
            Loan loan2 = new Loan("personal", 100000.0, payments2);
            Loan loan3 = new Loan("automotriz", 300000.0, payments3);
            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);

            ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, client, loan1);
            ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, client, loan2);

            ClientLoan clientLoan3 = new ClientLoan(100000.0, 24, client1, loan2);
            ClientLoan clientLoan4 = new ClientLoan(200000.0, 36, client1, loan3);

            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);
            clientLoanRepository.save(clientLoan3);
            clientLoanRepository.save(clientLoan4);

            Card card1 = new Card(client.toString(), CardType.DEBIT, CardColor.GOLD,
                    "0989 8938 3328 3383", 434, LocalDate.now(), LocalDate.now().plusYears(5));

            client.addCard(card1);
            cardRepository.save(card1);

            Card card2 = new Card(client.toString(), CardType.CREDIT, CardColor.TITANIUM,
                    "0984 6789 8765 4883", 784, LocalDate.now(), LocalDate.now().plusYears(5));

            client.addCard(card2);
            cardRepository.save(card2);

            Card card3 = new Card(client1.toString(), CardType.CREDIT, CardColor.SILVER,
                    "0432 5683 8765 5883", 167, LocalDate.now(), LocalDate.now().plusYears(5));

            client1.addCard(card3);
            cardRepository.save(card3);


            Client admin = clientRepository.save(new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin")));


        };
    }
}
package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;



@RestController
@RequestMapping("/api")
public class TransactionController {


        @Autowired
        TransactionRepository transactionRepository;


        @RequestMapping("/transactions")
        public List<TransactionDTO> getTransactions(){
            return transactionRepository.findAll().stream()
                    .map(TransactionDTO::new).collect(toList());
        }

        @RequestMapping("/transactions/{id}")
        public TransactionDTO getTransactionById(@PathVariable Long id){
            Optional<Transaction> transaction = transactionRepository.findById(id);
            return new TransactionDTO(transaction.get());
        }
}

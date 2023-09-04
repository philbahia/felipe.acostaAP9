package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {

    void addTransaction(Transaction transaction);

    List<TransactionDTO> getTransactions();

    Transaction findById(Long id);

}

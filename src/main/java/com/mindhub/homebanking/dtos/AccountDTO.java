package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;

    private String number;
    private LocalDate date;
    private double balance;

    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(){

    }

    public AccountDTO(Account account){

        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getDate();
        this.balance = account.getBalance();
        this.transactions = account
                .getTransactions()
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toSet());

    }

    public Long getId() {
        return id;
    }


    public String getNumber() {
        return number;
    }

    public LocalDateTime getDate() {

        return date.atTime(LocalTime.now());
    }

    public LocalDateTime getCreationDate() {
        return date.atTime(LocalTime.now());
    }

    public double getBalance() {
        return balance;
    }


    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}

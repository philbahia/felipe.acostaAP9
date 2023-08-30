package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTO {


    private Long id;

    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    private Account ownerTransaction;

    public TransactionDTO(Transaction transaction){
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();

    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }


    public double getAmount() {
        return amount;
    }


    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }


}

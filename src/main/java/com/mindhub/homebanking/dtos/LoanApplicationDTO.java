package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payments;
    private String numberAccount;
    private int interest;

    public LoanApplicationDTO() {
    }
    public LoanApplicationDTO(long id, double amount, int payments, String numberAccount,int interest){
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.numberAccount = numberAccount;
        this.interest = interest;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public int getInterest() {
        return interest;
    }
}

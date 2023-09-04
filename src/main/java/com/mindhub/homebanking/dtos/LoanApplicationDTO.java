package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {
    private Long loanId;
    private double amount;
    private Integer payments;
    private String toAccountNumber;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(Long loanId, double amount, int payments, String toAccountNumber){
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
        //this.interest = interest;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

   /* public int getInterest() {
        return interest;
    }*/
}

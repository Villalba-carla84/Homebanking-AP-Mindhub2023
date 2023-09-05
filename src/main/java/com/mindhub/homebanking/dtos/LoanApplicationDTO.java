package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private String loanId;
    private double amount;
    private Integer payments;
    private String  destinationAccountNumber;

    public String getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
}

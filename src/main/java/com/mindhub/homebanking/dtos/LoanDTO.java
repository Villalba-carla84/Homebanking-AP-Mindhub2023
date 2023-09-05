package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

public class LoanDTO {

    private long id;
    private String name;
    private Double maxAmount;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }
}

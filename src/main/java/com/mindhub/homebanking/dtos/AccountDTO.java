package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
public class AccountDTO {
    private long id;

    private String number;

    private LocalDate date;

    private double balance;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getCreationDate();
        this.balance = account.getBalance();
    }

    public long getId() {return id;}

    public String getNumber() {return number;}

    public LocalDate getDate() {return date;}

    public double getBalance() {return balance;}
}
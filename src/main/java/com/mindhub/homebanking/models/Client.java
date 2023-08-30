package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RolUser rol;

@OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
private Set<Account> accounts = new HashSet<>();

@OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
private Set<ClientLoan> clientLoans = new HashSet<>();

@OneToMany(mappedBy = "cards",fetch =  FetchType.EAGER)
private Set<Card> cardsClient =new HashSet<>();

    public Client(){}
    public Client( String firstName, String lastName, String email,String password, RolUser rol){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email= email;
        this.password = password;
        this.rol= rol;
    }



    public Set<Account> getAccounts() {

        return accounts;
    }
    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }


    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
    public Long getId() {
        return id;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public Set<ClientLoan> getClientLoans() {

        return clientLoans;
    }

    public Set<ClientLoan> getLoan() {

        return clientLoans;
    }

    public RolUser getRol() {
        return rol;
    }

    public void setRol(RolUser rol) {
        this.rol = rol;
    }

    public void addClientLoans(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<Card> getCardsClient() {

        return cardsClient;
    }
    public void addCard(Card card){
        card.setCards(this);
        cardsClient.add(card);
    }


    @JsonIgnore
    public List<Loan> getLoans(){
        return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());

    }

}

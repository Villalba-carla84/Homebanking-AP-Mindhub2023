package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

     @Autowired
    TransactionRepository transactionRepository;

    @Test
//busca todos los préstamos y luego verificar que la lista no esté vacía
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));

    }
    @Test
    //verifica que en la lista de los préstamos exista uno llamado “Personal
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }


    @Test
    //verifica que en la lista de los préstamos exista uno llamado “Hipotecario"
    public void existHipotLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Hipotecario"))));
    }

    @Test
    //verifica que en la lista de los préstamos exista uno llamado “Automotriz"
    public void existAutomotrizLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Automotriz"))));
    }
    @Test
    public void existsTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }



}
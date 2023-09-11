package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getListAccountsDTO (){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public AccountDTO getAccountById( Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);

    }
    @Override
    public void saveAccount(Account account) {

        accountRepository.save(account);
    }
    @Override
    public Account findAccount(String number){
        return accountRepository.findByNumber(number);

    }

     }




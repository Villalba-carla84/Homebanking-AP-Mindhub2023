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

    @Autowired
    public List<AccountDTO> getListAccountsDTO (){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Autowired
    public AccountDTO getAccountById( Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);

    }
    @Autowired
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
    @Autowired
    public Account findAccount(String number){
        return accountRepository.findByNumber(number);

    }

     }




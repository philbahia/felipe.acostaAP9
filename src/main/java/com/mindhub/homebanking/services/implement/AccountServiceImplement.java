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
    AccountRepository accountRepository;

    @Override
    public Account findByNumber(String numberAccount) {
        return accountRepository.findByNumber(numberAccount);
    }

    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void addAccount(Account account) {
        accountRepository.save(account);
    }


}

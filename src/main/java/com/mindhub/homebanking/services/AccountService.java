package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    Account findByNumber(String numberAccount);

    List<AccountDTO> getAccounts();

    Account findById(Long id);

    void addAccount(Account account);
}

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    //private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;
    //private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }

//modif

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if (account.getOwnerAccount().equals(client)){
            AccountDTO accountDTO = new AccountDTO(account);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTO);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account no Available");
        }

    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<Object> getCurrentClientAccounts(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());

        if (client != null) {
            List<AccountDTO> accountDTOs = client.getAccounts().stream()
                    .map(AccountDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(accountDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        // Consulto x el cliente con sesión iniciada
        Client client = clientService.findByEmail(authentication.getName());

        // Verifico el límite de cuentas por cliente
        if (client.getAccounts().size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Client already has the maximum allowed accounts");
        }

        String accountNumber = generateAccountNumber();

        Account newAccount = new Account(accountNumber,LocalDate.now(),0.0);
        client.addAccount(newAccount);
        accountService.addAccount(newAccount);



        return ResponseEntity.status(HttpStatus.CREATED).body("Account Created");

    }

    private String generateAccountNumber() {
        // Genera un número aleatorio de 6 dígitos
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return "VIN-" + randomNumber;
    }





}
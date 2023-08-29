package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream()
                .map(AccountDTO::new).collect(toList());
    }

//modif

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);

        if (account.getOwnerAccount().equals(client)){
            AccountDTO accountDTO = new AccountDTO(account);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountDTO);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account no Available");
        }

    }



    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        // Consulto x el cliente con sesión iniciada
        Client client = clientRepository.findByEmail(authentication.getName());

        // Verifico el límite de cuentas por cliente
        if (client.getAccounts().size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Client already has the maximum allowed accounts");
        }

        // Genera un número de cuenta aleatorio
        String accountNumber = generateAccountNumber();



        // Crea una nueva instancia de Account
        Account newAccount = new Account(accountNumber,LocalDate.now(),0.0);

        client.addAccount(newAccount);

        // Guarda la cuenta a través del repositorio
        accountRepository.save(newAccount);


        return ResponseEntity.status(HttpStatus.CREATED).body("Account Created");

    }

    private String generateAccountNumber() {
        // Genera un número aleatorio de 6 dígitos
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return "VIN-" + randomNumber;
    }





}
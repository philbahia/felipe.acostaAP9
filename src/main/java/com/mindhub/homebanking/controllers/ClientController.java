package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import java.util.Random;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    //private ClientRepository clientRepository;
    private ClientService clientService;

    @Autowired
   // private AccountRepository accountRepository;
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClients();
    }//review


    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClientNew(@PathVariable Long id, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
        Client clientA = clientService.findById(id);

        if (clientA.equals(client)){
            ClientDTO clientDTO = new ClientDTO(clientA);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientDTO);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Client no Available");
        }

    }


    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {



        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing data");
        }

        if (clientService.findByEmail(email) !=  null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Name already in use");
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.addClient(client);

        // TODO: 28/8/2023 Crear cuenta inicial
        // Genera un número de cuenta aleatorio
        String accountNumber = generateAccountNumber();

        Account account = new Account(accountNumber, LocalDate.now(), 0.0);
        client.addAccount(account);

        //accountRepository.save(account);
        accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account Created");

    }

    private String generateAccountNumber() {
        // Genera un número aleatorio de 6 dígitos
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return "VIN-" + randomNumber;
    }

}


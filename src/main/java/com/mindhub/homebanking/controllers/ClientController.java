package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }


    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClientNew(@PathVariable Long id, Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        Client clientA = clientRepository.findById(id).orElse(null);

        if (clientA.equals(client)){
            ClientDTO clientDTO = new ClientDTO(clientA);
            return new ResponseEntity<>(clientDTO,HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>("Client no Available",HttpStatus.CONFLICT);
        }

    }


    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {



        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);

        // TODO: 28/8/2023 Crear cuenta inicial
        // Genera un número de cuenta aleatorio
        String accountNumber = generateAccountNumber();

        Account account = new Account(accountNumber, LocalDate.now(), 0.0);
        client.addAccount(account);

        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    private String generateAccountNumber() {
        // Genera un número aleatorio de 6 dígitos
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return "VIN-" + randomNumber;
    }

}


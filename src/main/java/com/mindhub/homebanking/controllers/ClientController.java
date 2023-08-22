package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        Optional<Client> client = clientRepository.findById(id);

        return new ClientDTO(client.get());

    }
    @RequestMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}


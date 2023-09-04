package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    Client findByEmail(String email);

    List<ClientDTO> getClients();

    void addClient (Client client);

    Client findById(Long id);

}

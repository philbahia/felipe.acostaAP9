package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.models.TransactionType;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;



@RestController
@RequestMapping("/api")
public class TransactionController {


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;




    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.getTransactions();
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransactionById(@PathVariable Long id){
        return new TransactionDTO(transactionService.findById(id));
    }

    @Transactional
    @RequestMapping(path = "/transactions",method = RequestMethod.POST)
    public ResponseEntity<Object> createdTransaction(Authentication authentication,
                                                     @RequestParam Double amount,
                                                     @RequestParam String description,
                                                     @RequestParam String fromAccountNumber,
                                                     @RequestParam String toAccountNumber){




        //Verificar que los parámetros no estén vacíos
        if (amount == null || description.isBlank() || fromAccountNumber.isBlank() || toAccountNumber.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing or empty parameters");
        }
        //Verificar que los números de cuenta no sean iguales
        if (fromAccountNumber.equals(toAccountNumber)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sender and receiver accounts cannot be the same");
        }
        //Verificar que exista la cuenta de origen
        Account senderAccount = accountService.findByNumber(fromAccountNumber);

        if (senderAccount == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sender account not found");
        }

        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        // Obtener el cliente autenticado
        // Verificar si la cuenta de origen pertenece al cliente autenticado
        if (!senderAccount.getOwnerAccount().getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sender account does not belong to the authenticated client");
        }

        //Verificar que exista la cuenta de destino
        Account receiverAccount = accountService.findByNumber(toAccountNumber);

        if (receiverAccount == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Receiver account not found");
        }

        //Verificar que la cuenta de origen tenga el monto disponible
        if (senderAccount.getBalance() < amount) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient balance in the sender account");
        }

        //Se deben crear dos transacciones, una con el tipo de transacción “DEBIT”
        // asociada a la cuenta de origen
        Transaction debitTransfer = new Transaction();
        debitTransfer.setAmount(-amount);  // Monto negativo para el débito
        debitTransfer.setDescription(description);
        debitTransfer.setAccount(senderAccount);
        debitTransfer.setType(TransactionType.DEBIT);
        debitTransfer.setDate(LocalDateTime.now());


        // y la otra con el tipo de transacción “CREDIT”
        // asociada a la cuenta de destino
        Transaction creditTransfer = new Transaction();
        creditTransfer.setAmount(amount);
        creditTransfer.setDescription(description);
        creditTransfer.setAccount(receiverAccount);
        creditTransfer.setType(TransactionType.CREDIT);
        creditTransfer.setDate(LocalDateTime.now());

        //A la cuenta de origen se le restará el monto indicado
        //en la petición y a la cuenta de destino se le sumará el mismo monto
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        accountService.addAccount(senderAccount);
        accountService.addAccount(receiverAccount);

        transactionService.addTransaction(debitTransfer);
        transactionService.addTransaction(creditTransfer);


        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully");

    }

}

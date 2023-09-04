package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @RequestMapping(path = "/loans", method = RequestMethod.GET)
    public List<LoanDTO> getLoans(){
        //return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
        return loanService.getLoans();
    }


    @Transactional
    @RequestMapping(path="/loans",method = RequestMethod.POST)
    public ResponseEntity<String> requestLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                              Authentication authentication){


        if (authentication == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No authenticated");
        }
        // Verificar que loanApplicationDTO no sea nulo
        if (loanApplicationDTO == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid request: Missing data");
        }

        //Verificar que los datos sean correctos, es decir no estén vacíos,
        //        // que el monto no sea 0 o que las cuotas no sean 0.
        if (loanApplicationDTO.getLoanId() == null ||
                loanApplicationDTO.getAmount() <= 0 ||
                loanApplicationDTO.getPayments() <= 0 ||
                loanApplicationDTO.getToAccountNumber() == null )
                 {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid request: Missing or invalid parameters");
        }

        //Client client = clientRepository.findByEmail(authentication.getName());
        Client client = clientService.findByEmail(authentication.getName());

        //Verificar que los datos sean correctos, es decir no estén vacíos,
        // que el monto no sea 0 o que las cuotas no sean 0.
        if (loanApplicationDTO.getAmount()==0 || loanApplicationDTO.getPayments()==0){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not Available Parameter");
        }


        //Verificar que el préstamo exista
        if (loanApplicationDTO == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Inexistent Loan");
        }

        //Verificar que el monto solicitado no exceda el monto máximo del préstamo

        //Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        Loan loan = loanService.findById(loanApplicationDTO.getLoanId());

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Monto del prestamo excede limite");
        }

        //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cantidad de cuotas no disponible");
        }

        //Verificar que la cuenta de destino exista
        //Account toAccount = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        Account toAccount = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());

        if (toAccount == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe cuenta destino");
        }

        //Verificar que la cuenta de destino pertenezca al cliente autenticado
        if (!toAccount.getOwnerAccount().getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sender account does not belong to the authenticated client");
        }

        //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        double totalLoan;
        totalLoan = loanApplicationDTO.getAmount()*1.2;

        //Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo)
        // con la descripción concatenando el nombre del préstamo y la frase “loan approved”

        Transaction loanTransfer = new Transaction();
        loanTransfer.setAccount(toAccount);
        loanTransfer.setDescription(toAccount.toDetailsAproved());
        loanTransfer.setAmount(loanApplicationDTO.getAmount());
        loanTransfer.setType(TransactionType.CREDIT);
        loanTransfer.setDate(LocalDateTime.now());

        //public ClientLoan(double amount, Integer payments, Client client, Loan loan) {
        ClientLoan newLoan = new ClientLoan();
        newLoan.setAmount(totalLoan);
        newLoan.setPayments(loanApplicationDTO.getPayments());
        newLoan.setClient(client);
        newLoan.setLoan(loan);

        //clientLoanRepository.save(newLoan);
        clientLoanService.addClientLoan(newLoan);


        //Se debe actualizar la cuenta de destino sumando el monto solicitado

        toAccount.setBalance(toAccount.getBalance()+ loanApplicationDTO.getAmount());
        toAccount.addTransaction(loanTransfer);

        //transactionRepository.save(loanTransfer);
        transactionService.addTransaction(loanTransfer);



        return ResponseEntity.status(HttpStatus.CREATED).body("Joya Tenes la platita");
    }
}

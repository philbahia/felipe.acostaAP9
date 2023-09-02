package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {


    @Autowired
    private LoanRepository loanRepository;

    @RequestMapping(path = "/loans", method = RequestMethod.GET)
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll()
                .stream()
                .map(LoanDTO::new)
                .collect(toList());
    }


    @Transactional
    @RequestMapping(path="/loans",method = RequestMethod.POST)
    public ResponseEntity<String> createdLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                              Authentication authentication){

        //Verificar que los datos sean correctos, es decir no estén vacíos,
        // que el monto no sea 0 o que las cuotas no sean 0.
        if (loanApplicationDTO.getAmount()==0 || loanApplicationDTO.getPayments()==0){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not Available Parameter");
        }


        return ResponseEntity.status(HttpStatus.CREATED).body("Joya");
    }
}

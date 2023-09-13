package com.mindhub.homebanking.models;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;


import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class  RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    


    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("personal"))));
        assertThat(loans, hasItem(hasProperty("name", is("hipotecario"))));
        assertThat(loans, hasItem(hasProperty("name", is("automotriz"))));
    }



}


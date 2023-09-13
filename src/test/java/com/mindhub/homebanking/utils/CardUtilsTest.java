package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CardUtilsTest {

    @Test
    public void randomCardNumber() {

    String cardNumberTest = CardUtils.randomCardNumber();
    assertThat(cardNumberTest, is(not(emptyOrNullString())));
    }

}
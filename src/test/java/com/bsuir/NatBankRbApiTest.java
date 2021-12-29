package com.bsuir;

import com.bsuir.clients.CurrencyApiClient;
import com.bsuir.models.Currency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NatBankRbApiTest {

    @Autowired
    private CurrencyApiClient natBankRbApiClient;

    @Test
    public void test() {
        Currency byn = natBankRbApiClient.getCurrency("BYN");
        assertEquals("BYN", byn.getCurrencyStr());
        assertFalse(byn.getRates().isEmpty());
    }

}

package com.bsuir;

import com.bsuir.clients.CurrencyApiClient;
import com.bsuir.models.Currency;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FreeCurrencyApiTest {

    @Autowired
    private CurrencyApiClient freeCurrencyApiClient;

    private static final String currencyString = "USD";

    @Test
    public void test() {
        Currency currency = freeCurrencyApiClient.getCurrency(currencyString);
        log.debug("Currency: {}", currency);
        assertEquals(currencyString, currency.getCurrencyStr());
        assertFalse(currency.getRates().isEmpty());
    }

}

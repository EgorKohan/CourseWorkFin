package com.bsuir.services;

import com.bsuir.clients.CurrencyApiClient;
import com.bsuir.models.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class CurrencyService {

    private final List<CurrencyApiClient> currencyApiClientList;

    @Autowired
    public CurrencyService(List<CurrencyApiClient> currencyApiClientList) {
        this.currencyApiClientList = currencyApiClientList;
    }

    @PostConstruct
    public void init() {
        getCurrencies();
    }

    public void getCurrencies() {
        currencyApiClientList.forEach(currencyApiClient -> {
            List<String> supportedCurrencies = currencyApiClient.getSupportedCurrencies();
            supportedCurrencies.forEach(currency -> {
                Currency currencyObj = currencyApiClient.getCurrency(currency);
                log.info("CurrencyObj: {}", currencyObj);
            });
        });
    }

}

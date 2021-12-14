package com.bsuir.services;

import com.bsuir.clients.CurrencyApiClient;
import com.bsuir.models.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final List<CurrencyApiClient> currencyApiClientList;

    @Autowired
    public CurrencyServiceImpl(List<CurrencyApiClient> currencyApiClientList) {
        this.currencyApiClientList = currencyApiClientList;
    }

    @Scheduled(cron = "* */12 * * * *")
    public void scheduleRefreshing() {
        log.info("Start of getting currencies");
        try {
            for (CurrencyApiClient currencyApiClient : currencyApiClientList) {
                List<String> supportedCurrencies = currencyApiClient.getSupportedCurrencies();
                for (String currency : supportedCurrencies) {
                    Currency currencyObj = currencyApiClient.getCurrency(currency);
                    log.info("CurrencyObj: {}", currencyObj);
                }
            }
        } catch (RestClientException restClientException){
            log.error("Something went wrong while GET currencies");
        }
        log.info("End of getting currencies");
    }

}

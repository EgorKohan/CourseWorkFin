package com.bsuir.services;

import com.bsuir.clients.CurrencyApiClient;
import com.bsuir.models.Currency;
import com.bsuir.repositories.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final List<CurrencyApiClient> currencyApiClientList;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(List<CurrencyApiClient> currencyApiClientList, CurrencyRepository currencyRepository) {
        this.currencyApiClientList = currencyApiClientList;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Page<Currency> getAll(Pageable pageable) {
        return currencyRepository.findAll(pageable);
    }

    @Scheduled(cron = "0 */12 * * * *")
    public void scheduleRefreshing() {
        log.info("Start of getting currencies");
        try {
            for (CurrencyApiClient currencyApiClient : currencyApiClientList) {
                List<String> supportedCurrencies = currencyApiClient.getSupportedCurrencies();
                for (String currency : supportedCurrencies) {
                    Currency currencyObj = currencyApiClient.getCurrency(currency);
                    currencyApiClient.saveCurrencyInRepository(currencyRepository, currencyObj);
                }
            }
        } catch (RestClientException restClientException) {
            log.error("Something went wrong while GET currencies");
        }
        log.info("End of getting currencies");
    }

    @Override
    public Currency findByCurrency(String currency) {
        return currencyRepository.findById(currency).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency wasn't found"));
    }

    @Override
    public boolean isCurrencyExist(String currency) {
        return currencyRepository.existsById(currency);
    }

    @Override
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

}

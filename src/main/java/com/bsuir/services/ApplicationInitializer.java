package com.bsuir.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ApplicationInitializer {

    private final CurrencyService currencyService;
    private final DepositService depositService;

    @Autowired
    public ApplicationInitializer(CurrencyService currencyService, DepositService depositService) {
        this.currencyService = currencyService;
        this.depositService = depositService;
    }

    @PostConstruct
    public void init() {
        try {
            currencyService.scheduleRefreshing();
            depositService.scheduledRefreshing();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

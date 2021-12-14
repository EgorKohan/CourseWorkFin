package com.bsuir.clients;

import com.bsuir.models.Currency;
import com.bsuir.repositories.CurrencyRepository;

import java.util.List;

public interface CurrencyApiClient {

    Currency getCurrency(String currency);

    List<String> getSupportedCurrencies();

    void saveCurrencyInRepository(CurrencyRepository currencyRepository, Currency currency);

}

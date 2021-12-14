package com.bsuir.clients;

import com.bsuir.models.Currency;

import java.util.List;

public interface CurrencyApiClient {

    Currency getCurrency(String currency);

    List<String> getSupportedCurrencies();

}

package com.bsuir.services;

import com.bsuir.models.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CurrencyService {

    Page<Currency> getAll(Pageable pageable);

    void scheduleRefreshing();

    Currency findByCurrency(String currency);

}

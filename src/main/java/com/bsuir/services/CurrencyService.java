package com.bsuir.services;

import com.bsuir.models.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CurrencyService {

    Page<Currency> getAll(Pageable pageable);

    void scheduleRefreshing();

    Currency findByCurrency(String currency);

    boolean isCurrencyExist(String currency);

    List<Currency> getAll();

}

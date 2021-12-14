package com.bsuir.api.v1.dtos;

import com.bsuir.models.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {

    private String currency;
    private Map<String, Double> rates;

    public static CurrencyDto toDto(Currency currencyObj) {
        return new CurrencyDto(currencyObj.getCurrency(), currencyObj.getRates());
    }

    public static Currency toCurrency(CurrencyDto currencyDtoObj) {
        return new Currency(currencyDtoObj.getCurrency(), currencyDtoObj.getRates());
    }

}

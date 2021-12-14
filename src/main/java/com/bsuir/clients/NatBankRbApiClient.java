package com.bsuir.clients;

import com.bsuir.models.Currency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NatBankRbApiClient implements CurrencyApiClient {

    @Value("${rate_exchanger.api.nat_bank_rb.url}")
    private String url;

    @Override
    public Currency getCurrency(String currency) {
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        return null;
    }

}

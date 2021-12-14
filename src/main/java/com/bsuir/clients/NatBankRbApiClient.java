package com.bsuir.clients;

import com.bsuir.models.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NatBankRbApiClient implements CurrencyApiClient {

    @Value("${rate_exchanger.api.nat_bank_rb.url}")
    private String url;

    @Value("${rate_exchanger.api.nat_bank_rb.supported_currencies}")
    @Getter
    private List<String> supportedCurrencies;

    @Override
    public Currency getCurrency(String currency) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return new Currency(currency, getRates(response));
    }

    private Map<String, Double> getRates(String response) {
        Map<String, Double> rates = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readTree(response).elements().forEachRemaining(jsonNode -> {
                rates.put(jsonNode.get("Cur_Abbreviation").asText(), jsonNode.get("Cur_OfficialRate").asDouble());
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rates;
    }

}

package com.bsuir.clients;

import com.bsuir.models.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class FreeCurrencyApiClient implements CurrencyApiClient {

    @Value("${rate_exchanger.api.free_currencyapi.url}")
    private String url;

    @Value("${rate_exchanger.api.free_currencyapi.key}")
    private String key;

    @Override
    public Currency getCurrency(String currency) {
        RestTemplate restTemplate = new RestTemplate();
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("apikey", key)
                .queryParam("base_currency", currency)
                .toUriString();
        String response = restTemplate.getForObject(urlTemplate, String.class);
        return new Currency(currency, getRatesFromResponse(response));
    }

    private Map<String, Double> getRatesFromResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode data = objectMapper.readTree(response).get("data");
            return objectMapper.convertValue(data, new TypeReference<Map<String, Double>>() {
            });
        } catch (JsonProcessingException e) {
            throw new DataAccessResourceFailureException("Cannot parse data from free currencyApi");
        }
    }

}

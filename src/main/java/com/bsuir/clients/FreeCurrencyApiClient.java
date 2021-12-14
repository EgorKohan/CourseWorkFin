package com.bsuir.clients;

import com.bsuir.models.Currency;
import com.bsuir.repositories.CurrencyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FreeCurrencyApiClient implements CurrencyApiClient {

    @Value("${rate_exchanger.api.free_currencyapi.url}")
    private String url;

    @Value("${rate_exchanger.api.free_currencyapi.key}")
    private String key;

    @Getter
    @Value("${rate_exchanger.api.free_currencyapi.supported_currencies}")
    private List<String> supportedCurrencies;

    @Override
    public Currency getCurrency(String currency) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("apikey", key)
                .queryParam("base_currency", currency)
                .toUriString();
        String response = restTemplate.getForObject(urlTemplate, String.class);
        return new Currency(currency, getRatesFromResponse(response));
    }

    @Override
    public void saveCurrencyInRepository(CurrencyRepository currencyRepository, Currency currency) {
        log.info("Start of saveCurrencyInRepository method for FreeCurrencyApiClient");
        currencyRepository.save(currency);
        log.info("End of saveCurrencyInRepository method for FreeCurrencyApiClient");
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

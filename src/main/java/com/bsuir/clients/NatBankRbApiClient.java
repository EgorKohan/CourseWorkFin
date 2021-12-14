package com.bsuir.clients;

import com.bsuir.models.Currency;
import com.bsuir.repositories.CurrencyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;

@Component
@Slf4j
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

    @Override
    public void saveCurrencyInRepository(CurrencyRepository currencyRepository, Currency currency) {
        log.info("Start of saveCurrencyInRepository method for NatBankRbApiClient");
        currencyRepository.save(currency);
        Set<Map.Entry<String, Double>> entries = currency.getRates().entrySet();
        entries.forEach(entry -> {
            Currency currencyById = currencyRepository.findById(entry.getKey()).orElse(null);
            if (!isNull(currencyById)) {
                currencyById.getRates().put("BYN", 1 / entry.getValue());
                log.info("Update {} currency. Set new BYN rate: {}", currencyById.getCurrency(), 1 / entry.getValue());
                currencyRepository.save(currencyById);
            }
        });
        log.info("End of saveCurrencyInRepository method for NatBankRbApiClient");
    }

    private Map<String, Double> getRates(String response) {
        Map<String, Double> rates = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readTree(response).elements().forEachRemaining(jsonNode -> {
                String curAbbreviation = jsonNode.get("Cur_Abbreviation").asText();
                double curScale = jsonNode.get("Cur_Scale").asDouble();
                double curOfficialRate = 1 / (jsonNode.get("Cur_OfficialRate").asDouble() / curScale);
                rates.put(curAbbreviation, curOfficialRate);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rates;
    }


}

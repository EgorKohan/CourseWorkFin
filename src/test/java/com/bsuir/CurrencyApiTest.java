package com.bsuir;

import com.bsuir.api.v1.dtos.DepositDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CurrencyApiTest {

    @Value("${deposits.getUrl}")
    private String getDepositsUrl;

    @SneakyThrows
    @Test
    public void checkThatDepositsReturn() {
        RestTemplate restTemplate = new RestTemplate();
        String deposits = restTemplate.getForObject(getDepositsUrl, String.class);
        List<DepositDto> depositDtos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readTree(deposits).elements()
                .forEachRemaining(jsonNode -> {
                    try {
                        depositDtos.add(objectMapper.readValue(jsonNode.toString(), DepositDto.class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        assertTrue(!depositDtos.isEmpty());
    }

}

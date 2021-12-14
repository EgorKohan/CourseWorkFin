package com.bsuir.services;

import com.bsuir.api.v1.dtos.DepositDto;
import com.bsuir.models.Deposit;
import com.bsuir.repositories.DepositRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepositServiceImpl implements DepositService {

    @Value("${deposits.getUrl}")
    private String getDepositsUrl;

    private final DepositRepository depositRepository;

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @PostConstruct
    public void init(){
        try {
            scheduledRefreshing();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Deposit> findAll() {
        return depositRepository.findAll();
    }

    @Override
    public Page<Deposit> findAllWPageable(Pageable pageable) {
        return depositRepository.findAll(pageable);
    }

    @Override
    public List<Deposit> findByCurrency(String currency) {
        return depositRepository.findAllByCurrency(currency);
    }

    @Override
    @Scheduled(cron = "0 */12 * * * *")
    public void scheduledRefreshing() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(getDepositsUrl, String.class);
        String depositsJson = restTemplate.getForObject(getDepositsUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Deposit> deposits = new ArrayList<>();
        objectMapper.readTree(depositsJson).elements()
                .forEachRemaining(jsonNode -> {
                    try {
                        deposits.add(DepositDto.toDeposit(objectMapper.readValue(jsonNode.toString(), DepositDto.class)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        depositRepository.saveAll(deposits);
    }
}

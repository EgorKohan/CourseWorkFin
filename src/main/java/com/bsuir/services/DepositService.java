package com.bsuir.services;

import com.bsuir.models.Deposit;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepositService {

    List<Deposit> findAll();

    Page<Deposit> findAllWPageable(Pageable pageable);

    List<Deposit> findByCurrency(String currency);

    void scheduledRefreshing() throws JsonProcessingException;

    Deposit findById(Long id);

}

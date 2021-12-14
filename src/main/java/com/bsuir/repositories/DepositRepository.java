package com.bsuir.repositories;

import com.bsuir.models.Deposit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends MongoRepository<Deposit, String> {

    List<Deposit> findAllByCurrency(String currency);

}

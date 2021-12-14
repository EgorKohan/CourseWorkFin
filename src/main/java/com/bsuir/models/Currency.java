package com.bsuir.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Currency {

    @MongoId
    private String currency;

    private Map<String, Double> rates;

}

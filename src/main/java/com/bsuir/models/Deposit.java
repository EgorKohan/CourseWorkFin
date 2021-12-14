package com.bsuir.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@Document
public class Deposit {

    @MongoId
    private Long _id;

    private String name;
    private String description;
    private String currency;
    private int duration;
    private double percent;

}

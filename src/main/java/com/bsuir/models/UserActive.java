package com.bsuir.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@EqualsAndHashCode(of = {"currency"})
public class UserActive {

    @MongoId
    private String _id;

    private double amount;
    private String currency;

}

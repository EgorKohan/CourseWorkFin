package com.bsuir.models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

@Document
@Data
public class User implements Serializable {

    @MongoId
    private String _id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private Status status;

}

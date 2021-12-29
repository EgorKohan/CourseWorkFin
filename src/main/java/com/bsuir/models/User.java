package com.bsuir.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @MongoId
    private ObjectId _id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private Status status;

    private Set<UserActive> userActives;

}

package com.bsuir.services;

import com.bsuir.api.v1.dtos.AuthDto;
import com.bsuir.models.User;

import java.util.Map;

public interface AuthUserService {

    User fillNewUserWithDefaultData(User user);

    Map<String, String> generateJwtToken(AuthDto authDto);

}

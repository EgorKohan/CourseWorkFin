package com.bsuir.services;

import com.bsuir.models.UserActive;

import java.util.Set;

public interface UserActiveService {

    Set<UserActive> addActiveToUser(String username, UserActive userActive);

    Set<UserActive> getActiveListByUsername(String username);

}

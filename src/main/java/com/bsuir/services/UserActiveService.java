package com.bsuir.services;

import com.bsuir.models.UserActive;

import java.util.List;
import java.util.Set;

public interface UserActiveService {

    Set<UserActive> addActiveToUser(String username, UserActive userActive);

    Set<UserActive> addActivesToUser(String username, List<UserActive> userActiveList);

    Set<UserActive> getActiveListByUsername(String username);

    Set<UserActive> deleteActiveForUser(String username, UserActive userActive);
}

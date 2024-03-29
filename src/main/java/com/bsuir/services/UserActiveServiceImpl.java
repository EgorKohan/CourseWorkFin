package com.bsuir.services;

import com.bsuir.models.User;
import com.bsuir.models.UserActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.SetUtils.emptyIfNull;

@Service
public class UserActiveServiceImpl implements UserActiveService {

    private final UserService userService;

    @Autowired
    public UserActiveServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Set<UserActive> addActiveToUser(String username, UserActive userActive) {
        User user = userService.findUserByUsername(username);
        Set<UserActive> userActives = user.getUserActives();
        if (isNull(userActives)) {
            Set<UserActive> newActives = new HashSet<>();
            newActives.add(userActive);
            user.setUserActives(newActives);
        } else {
            userActives.remove(userActive);
            userActives.add(userActive);
        }
        return userService.save(user).getUserActives();
    }

    @Override
    public Set<UserActive> addActivesToUser(String username, List<UserActive> userActiveList) {
        User user = userService.findUserByUsername(username);
        Set<UserActive> userActives = user.getUserActives();
        if (isNull(userActives)) {
            Set<UserActive> newActives = new HashSet<>(userActiveList);
            user.setUserActives(newActives);
        } else {
            userActiveList.forEach(userActives::remove);
            userActives.addAll(userActiveList);
        }
        return userService.save(user).getUserActives();
    }

    @Override
    public Set<UserActive> getActiveListByUsername(String username) {
        return emptyIfNull(userService.findUserByUsername(username).getUserActives());
    }

    @Override
    public Set<UserActive> deleteActiveForUser(String username, UserActive userActive) {
        User user = userService.findUserByUsername(username);
        Set<UserActive> userActives = user.getUserActives();
        userActives.remove(userActive);
        return userService.save(user).getUserActives();
    }
}

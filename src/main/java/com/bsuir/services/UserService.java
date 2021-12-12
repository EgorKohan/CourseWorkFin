package com.bsuir.services;

import com.bsuir.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findUserByUsername(String username);

    User create(User user);

    User update(User user);

    boolean delete(User user);

    boolean deleteByUsername(String username);

    UserDetails loadUserByUsername(String username);

}

package com.bsuir.services;

import com.bsuir.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    Page<User> findAllWPagination(Pageable pageable);

    User findUserByUsername(String username);

    User save(User user);

    boolean delete(User user);

    boolean deleteByUsername(String username);

    UserDetails loadUserByUsername(String username);

    void checkThatUsernameIsUnique(String username);

    boolean isUserExists(String username);

}

package com.bsuir.services;

import com.bsuir.exceptions.UserNotFoundException;
import com.bsuir.models.SecurityUser;
import com.bsuir.models.User;
import com.bsuir.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findUserByUsername(username);
        return new SecurityUser(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findFirstByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User create(User user) {
        return userRepository.insert(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean delete(User user) {
        userRepository.delete(user);
        return true;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return userRepository.deleteByUsername(username);
    }
}

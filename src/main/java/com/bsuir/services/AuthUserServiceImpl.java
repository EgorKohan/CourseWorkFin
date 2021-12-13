package com.bsuir.services;

import com.bsuir.api.v1.dtos.AuthDto;
import com.bsuir.models.Role;
import com.bsuir.models.Status;
import com.bsuir.models.User;
import com.bsuir.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthUserServiceImpl(
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public User fillNewUserWithDefaultData(User user) {
        user.setStatus(Status.ACTIVE);
        user.setRoles(Collections.singletonList(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @Override
    public Map<String, String> generateJwtToken(AuthDto authDto) {
        String username = authDto.getUsername();

        String password = authDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userService.findUserByUsername(username);
        String token = jwtTokenProvider.generateToken(username, user.getRoles());

        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);
        return response;
    }

}

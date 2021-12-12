package com.bsuir.api.v1.rest.controllers;

import com.bsuir.api.v1.dtos.AuthDto;
import com.bsuir.api.v1.dtos.UserDto;
import com.bsuir.models.Role;
import com.bsuir.models.Status;
import com.bsuir.models.User;
import com.bsuir.security.JwtTokenProvider;
import com.bsuir.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthDto authDto) {
        String username = authDto.getUsername();

        String password = authDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userService.findUserByUsername(username);
        String token = jwtTokenProvider.generateToken(username, user.getRoles());

        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/authorize")
    public UserDto authorize(@Valid @RequestBody UserDto userDto) {
        User user = UserDto.toUser(userDto);
        user.setStatus(Status.ACTIVE);
        user.setRoles(Collections.singletonList(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserDto.toDto(userService.create(user));
    }

}

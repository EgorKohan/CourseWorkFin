package com.bsuir.api.v1.rest.controllers;

import com.bsuir.api.v1.dtos.AuthDto;
import com.bsuir.api.v1.dtos.UserDto;
import com.bsuir.models.User;
import com.bsuir.services.AuthUserService;
import com.bsuir.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthUserService authUserService;

    @Autowired
    public AuthController(UserService userService, AuthUserService authUserService) {
        this.userService = userService;
        this.authUserService = authUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authUserService.generateJwtToken(authDto));
    }

    @PostMapping("/authorize")
    public UserDto authorize(@Valid @RequestBody UserDto userDto) {
        User user = UserDto.toUser(userDto);
        userService.checkThatUsernameIsUnique(user.getUsername());
        return UserDto.toDto(userService.save(authUserService.fillNewUserWithDefaultData(user)));
    }

}

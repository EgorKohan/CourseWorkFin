package com.bsuir.api.v1.rest.controllers;

import com.bsuir.api.v1.dtos.UserActiveDto;
import com.bsuir.services.UserActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/actives")
public class UserActiveController {

    private final UserActiveService userActiveService;

    @Autowired
    public UserActiveController(UserActiveService userActiveService) {
        this.userActiveService = userActiveService;
    }

    @PostMapping
    public Set<UserActiveDto> addActiveToUser(@RequestBody UserActiveDto userActiveDto, Principal principal) {
        return userActiveService.addActiveToUser(principal.getName(), UserActiveDto.toUserActive(userActiveDto))
                .stream()
                .map(UserActiveDto::toUserActiveDto)
                .collect(Collectors.toSet());
    }

    @GetMapping
    public Set<UserActiveDto> getAllUserActivesByUsername(Principal principal) {
        String username = principal.getName();
        return userActiveService.getActiveListByUsername(username)
                .stream()
                .map(UserActiveDto::toUserActiveDto)
                .collect(Collectors.toSet());
    }

}

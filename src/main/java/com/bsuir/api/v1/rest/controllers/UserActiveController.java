package com.bsuir.api.v1.rest.controllers;

import com.bsuir.api.v1.dtos.UserActiveDto;
import com.bsuir.finance.solver.FinanceSolver;
import com.bsuir.services.CurrencyService;
import com.bsuir.services.UserActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/actives")
public class UserActiveController {

    private final UserActiveService userActiveService;
    private final CurrencyService currencyService;
    private final FinanceSolver financeSolver;

    @Autowired
    public UserActiveController(UserActiveService userActiveService, CurrencyService currencyService, FinanceSolver financeSolver) {
        this.userActiveService = userActiveService;
        this.currencyService = currencyService;
        this.financeSolver = financeSolver;
    }

    @PostMapping
    public Set<UserActiveDto> addActiveToUser(@Valid @RequestBody UserActiveDto userActiveDto, Principal principal) {
        String currency = userActiveDto.getCurrency();
        if (!currencyService.isCurrencyExist(currency)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not supported currency");
        }
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

    @GetMapping("/calculate")
    public Map<String, Object> calculateInvestments(Principal principal) {
        return financeSolver.calculateInvestments(principal.getName());
    }

    @PostMapping("/delete")
    public Set<UserActiveDto> delete(@Valid @RequestBody UserActiveDto userActiveDto, Principal principal) {
        String currency = userActiveDto.getCurrency();
        if (!currencyService.isCurrencyExist(currency)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not supported currency");
        }
        return userActiveService.deleteActiveForUser(principal.getName(), UserActiveDto.toUserActive(userActiveDto))
                .stream()
                .map(UserActiveDto::toUserActiveDto)
                .collect(Collectors.toSet());
    }

}

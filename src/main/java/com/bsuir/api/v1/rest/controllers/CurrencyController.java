package com.bsuir.api.v1.rest.controllers;

import com.bsuir.api.v1.dtos.CurrencyDto;
import com.bsuir.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public List<CurrencyDto> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") @Min(1) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return currencyService.getAll(pageable).getContent().stream().map(CurrencyDto::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{currency}")
    public CurrencyDto getByCurrencyName(@PathVariable String currency) {
        return CurrencyDto.toDto(currencyService.findByCurrency(currency));
    }

}

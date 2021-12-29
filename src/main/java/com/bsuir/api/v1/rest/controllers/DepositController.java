package com.bsuir.api.v1.rest.controllers;

import com.bsuir.api.v1.dtos.DepositDto;
import com.bsuir.services.DepositService;
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
@RequestMapping("/api/v1/deposits")
public class DepositController {

    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping()
    public List<DepositDto> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") @Min(1) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return depositService.findAllWPageable(pageable).getContent().stream().map(DepositDto::toDepositDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DepositDto getDepositById(@PathVariable(required = false) Long id) {
        return DepositDto.toDepositDto(depositService.findById(id));
    }

}

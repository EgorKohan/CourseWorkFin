package com.bsuir.api.v1.dtos;

import com.bsuir.models.UserActive;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserActiveDto {

    private double amount;
    private String currency;

    public static UserActive toUserActive(UserActiveDto userActiveDto) {
        return new UserActive(
                null,
                userActiveDto.amount,
                userActiveDto.currency
        );
    }

    public static UserActiveDto toUserActiveDto(UserActive userActive) {
        return new UserActiveDto(
                userActive.getAmount(),
                userActive.getCurrency()
        );
    }

}

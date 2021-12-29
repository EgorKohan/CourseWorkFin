package com.bsuir.api.v1.dtos;

import com.bsuir.models.UserActive;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserActiveDto {

    @Min(value = 0, message = "Amount must be grater than 0")
    @NotNull(message = "Amount cannot be empty")
    private Double amount;
    @NotEmpty(message = "Currency cannot be empty")
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

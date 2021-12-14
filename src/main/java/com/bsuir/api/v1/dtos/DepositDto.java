package com.bsuir.api.v1.dtos;

import com.bsuir.models.Deposit;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositDto {

    @JsonAlias({"vklad_id"})
    private Long _id;
    @JsonAlias({"vklad_name"})
    private String name;
    @JsonAlias({"vklad_val"})
    private String currency;
    @JsonAlias({"vklad_srok"})
    private int duration;
    @JsonAlias({"vklad_procent"})
    private double percent;
    @JsonAlias({"vklad_info"})
    private String description;

    public static Deposit toDeposit(DepositDto depositDto) {
        return new Deposit(
                depositDto._id,
                depositDto.name,
                depositDto.description,
                depositDto.currency,
                depositDto.duration,
                depositDto.percent
        );
    }

    public static Deposit toDepositDto(Deposit deposit) {
        return new Deposit(
                null,
                deposit.getName(),
                deposit.getDescription(),
                deposit.getCurrency(),
                deposit.getDuration(),
                deposit.getPercent()
        );
    }

}

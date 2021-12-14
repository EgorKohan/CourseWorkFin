package com.bsuir.api.v1.dtos;

import com.bsuir.models.Deposit;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public static DepositDto toDepositDto(Deposit deposit) {
        return new DepositDto(
                deposit.get_id(),
                deposit.getName(),
                deposit.getCurrency(),
                deposit.getDuration(),
                deposit.getPercent(),
                deposit.getDescription()
        );
    }

}

package com.bsuir.api.v1.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(max = 10, message = "Username must be 0 to 10 symbols")
    protected String username;

    @Size(max = 20, message = "Username must be 0 to 20 symbols")
    @NotBlank(message = "Username cannot be empty")
    protected String password;

}

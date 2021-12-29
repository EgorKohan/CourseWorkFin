package com.bsuir.api.v1.dtos;

import com.bsuir.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto extends AuthDto {

    @NotBlank(message = "First name must be not empty")
    @Size(max = 20, message = "First name must be in 0 to 20 symbols")
    private String firstName;

    @NotBlank(message = "Last name must be not empty")
    @Size(max = 20, message = "Last name must be in 0 to 20 symbols")
    private String lastName;

    @NotBlank(message = "Email must be not empty")
    @Email
    private String email;

    public static UserDto toDto(User user) {
        UserDto userDto = UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .userActives(new HashSet<>())
                .build();
    }

}

package by.zapolski.english.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "Can't be blank")
    private String userName;

    @Email
    @NotBlank(message = "Can't be blank")
    private String email;

    @NotBlank(message = "Can't be blank")
    private String password;

    @NotBlank(message = "Can't be blank")
    private String passwordConfirmation;
}

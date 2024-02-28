package dev.codebusters.code_busters.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRegistrationRequest {

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    @NotBlank
    private String name;

    @Size(max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$", message = "Password should contain " +
            "at least one small letter, one capital letter, one digit, one special symbol @#$%^&+= " +
            "and have a minimum length of 8 characters")
    private String password;

}

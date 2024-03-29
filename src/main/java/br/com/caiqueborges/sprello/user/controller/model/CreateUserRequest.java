package br.com.caiqueborges.sprello.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @Email(message = "{user.email.valid}")
    @NotBlank(message = "{user.email.notblank}")
    private String email;

    @ToString.Exclude
    @Size(min = 8, message = "{user.password.size.min}")
    @NotBlank(message = "{user.password.notblank}")
    private String password;

    @Size(max = 80, message = "{user.firstname.size.max}")
    @NotBlank(message = "{user.firstname.notblank}")
    private String firstName;

    @Size(max = 300, message = "{user.lastname.size.max}")
    @NotBlank(message = "{user.lastname.notblank}")
    private String lastName;

}

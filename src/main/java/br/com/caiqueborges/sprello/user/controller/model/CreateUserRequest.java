package br.com.caiqueborges.sprello.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "{user.firstname.notblank}")
    private String firstName;

    @NotBlank(message = "{user.lastname.notblank}")
    private String lastName;

    @Email(message = "{user.email.valid}")
    @NotBlank(message = "{user.email.notblank}")
    private String email;

    @Size(min = 8, message = "{user.password.min}")
    @NotBlank(message = "{user.password.notblank}")
    private String password;

}

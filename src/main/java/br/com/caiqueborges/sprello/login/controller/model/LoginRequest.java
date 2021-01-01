package br.com.caiqueborges.sprello.login.controller.model;

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
public class LoginRequest {

    @Email(message = "{user.email.valid}")
    @NotBlank(message = "{user.email.notblank}")
    private String email;

    @ToString.Exclude
    @Size(min = 8, message = "{user.password.size.min}")
    @NotBlank(message = "{user.password.notblank}")
    private String password;

}

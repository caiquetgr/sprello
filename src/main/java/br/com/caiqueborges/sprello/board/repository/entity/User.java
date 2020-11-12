package br.com.caiqueborges.sprello.board.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Table(name = "users")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{user.firstname.notblank}", groups = ValidUserSave.class)
    private String firstName;

    @NotBlank(message = "{user.lastname.notblank}", groups = ValidUserSave.class)
    private String lastName;

    @Email(message = "{user.email.valid}", groups = ValidUserSave.class)
    @NotBlank(message = "{user.email.notblank}", groups = ValidUserSave.class)
    private String email;

    @Min(value = 8, message = "{user.password.min}", groups = ValidUserSave.class)
    @NotBlank(message = "{user.password.notblank}", groups = ValidUserSave.class)
    private String password;

    public interface ValidUserSave {
    }

}

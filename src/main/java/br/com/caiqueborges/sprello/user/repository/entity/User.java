package br.com.caiqueborges.sprello.user.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

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

    @Email(message = "{user.email.valid}", groups = ValidUserSave.class)
    @NotBlank(message = "{user.email.notblank}", groups = ValidUserSave.class)
    @Column(name = "email", unique = true)
    private String email;

    @ToString.Exclude
    @Size(min = 8, message = "{user.password.size.min}", groups = ValidUserSave.class)
    @NotBlank(message = "{user.password.notblank}", groups = ValidUserSave.class)
    private String password;

    @Size(max = 80, message = "{user.firstname.size.max}")
    @NotBlank(message = "{user.firstname.notblank}", groups = ValidUserSave.class)
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 300, message = "{user.lastname.size.max}")
    @NotBlank(message = "{user.lastname.notblank}", groups = ValidUserSave.class)
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    private Boolean active;

    public interface ValidUserSave {
    }

}

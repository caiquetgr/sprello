package br.com.caiqueborges.sprello.user.service;

import br.com.caiqueborges.sprello.user.repository.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
public interface CreateUserService {

    @Validated(User.ValidUserSave.class)
    User createUser(@Valid User user);

}

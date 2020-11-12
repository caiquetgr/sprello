package br.com.caiqueborges.sprello.user.service;

import br.com.caiqueborges.sprello.board.repository.entity.User;
import org.mapstruct.Mapper;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Mapper
public interface CreateUserService {

    @Validated(User.ValidUserSave.class)
    User createUser(@Valid User user);

}

package br.com.caiqueborges.sprello.user.service;

import br.com.caiqueborges.sprello.board.repository.entity.User;
import org.mapstruct.Mapper;

import javax.validation.Valid;

@Mapper
public interface CreateUserService {

    User createUser(@Valid User user);

}

package br.com.caiqueborges.sprello.user.controller.mapper;

import br.com.caiqueborges.sprello.user.controller.model.CreateUserRequest;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserResponse;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserControllerMapper {

    User createUserRequestToUser(CreateUserRequest createUserRequest);
    CreateUserResponse userToCreateUserResponse(User user);

}

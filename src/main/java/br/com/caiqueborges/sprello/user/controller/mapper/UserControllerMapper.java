package br.com.caiqueborges.sprello.user.controller.mapper;

import br.com.caiqueborges.sprello.board.repository.entity.User;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserRequest;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserResponse;
import org.mapstruct.Mapper;

@Mapper
public interface UserControllerMapper {

    User createUserRequestToUser(CreateUserRequest createUserRequest);
    CreateUserResponse userToCreateUserResponse(User user);

}

package br.com.caiqueborges.sprello.user.controller;

import br.com.caiqueborges.sprello.board.repository.entity.User;
import br.com.caiqueborges.sprello.user.controller.mapper.UserControllerMapper;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserRequest;
import br.com.caiqueborges.sprello.user.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final CreateUserService createUserService;
    private final UserControllerMapper mapper;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

        final User createdUser = createUserService.createUser(mapper.createUserRequestToUser(createUserRequest));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.userToCreateUserResponse(createdUser));

    }

}

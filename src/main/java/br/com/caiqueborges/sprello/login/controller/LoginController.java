package br.com.caiqueborges.sprello.login.controller;

import br.com.caiqueborges.sprello.login.controller.mapper.LoginControllerMapper;
import br.com.caiqueborges.sprello.login.controller.model.LoginRequest;
import br.com.caiqueborges.sprello.login.controller.model.LoginResponse;
import br.com.caiqueborges.sprello.login.service.LoginService;
import br.com.caiqueborges.sprello.login.service.model.AuthenticationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/login")
@RestController
public class LoginController {

    private final LoginService loginService;
    private final LoginControllerMapper loginControllerMapper;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        final AuthenticationInfo authenticationInfo = loginService
                .login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(loginControllerMapper.authenticationInfoToLoginResponse(authenticationInfo));


    }

}

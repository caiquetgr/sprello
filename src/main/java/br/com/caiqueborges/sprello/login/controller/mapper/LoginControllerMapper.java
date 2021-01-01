package br.com.caiqueborges.sprello.login.controller.mapper;

import br.com.caiqueborges.sprello.login.controller.model.LoginResponse;
import br.com.caiqueborges.sprello.login.service.model.AuthenticationInfo;
import org.mapstruct.Mapper;

@Mapper
public interface LoginControllerMapper {

    LoginResponse authenticationInfoToLoginResponse(AuthenticationInfo authenticationInfo);

}

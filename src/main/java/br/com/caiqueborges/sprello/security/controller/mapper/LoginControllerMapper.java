package br.com.caiqueborges.sprello.security.controller.mapper;

import br.com.caiqueborges.sprello.security.controller.model.LoginResponse;
import br.com.caiqueborges.sprello.security.service.model.AuthenticationInfo;
import org.mapstruct.Mapper;

@Mapper
public interface LoginControllerMapper {

    LoginResponse authenticationInfoToLoginResponse(AuthenticationInfo authenticationInfo);

}

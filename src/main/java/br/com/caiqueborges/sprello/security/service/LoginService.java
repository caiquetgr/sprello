package br.com.caiqueborges.sprello.security.service;

import br.com.caiqueborges.sprello.security.service.model.AuthenticationInfo;

public interface LoginService {

    AuthenticationInfo login(String email, String password);

}

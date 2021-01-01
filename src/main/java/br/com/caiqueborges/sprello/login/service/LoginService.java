package br.com.caiqueborges.sprello.login.service;

import br.com.caiqueborges.sprello.login.service.model.AuthenticationInfo;

public interface LoginService {

    AuthenticationInfo login(String email, String password);

}

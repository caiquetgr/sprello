package br.com.caiqueborges.sprello.security.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;

import java.util.Collections;
import java.util.Map;

public class LoginUserNotActiveException extends BaseException {

    @Override
    public String getMessageKey() {
        return "login.failed.inactive";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Collections.emptyMap();
    }

}

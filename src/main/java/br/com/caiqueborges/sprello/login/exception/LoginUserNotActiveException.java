package br.com.caiqueborges.sprello.login.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
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

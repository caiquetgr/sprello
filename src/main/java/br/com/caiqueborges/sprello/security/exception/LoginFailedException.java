package br.com.caiqueborges.sprello.security.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginFailedException extends BaseException {

    @Override
    public String getMessageKey() {
        return "login.failed";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Collections.emptyMap();
    }

}

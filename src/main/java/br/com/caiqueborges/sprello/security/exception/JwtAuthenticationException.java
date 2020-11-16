package br.com.caiqueborges.sprello.security.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtAuthenticationException extends BaseException {

    private final String message;

    public JwtAuthenticationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessageKey() {
        return message;
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Collections.emptyMap();
    }

}

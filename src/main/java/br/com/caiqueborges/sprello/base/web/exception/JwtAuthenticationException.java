package br.com.caiqueborges.sprello.base.web.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtAuthenticationException extends BaseException {

    @Override
    public String getMessageKey() {
        return "jwt.parse-exception";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Collections.emptyMap();
    }

}

package br.com.caiqueborges.sprello.base.web.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RequestWithoutAuthorizationException extends BaseException {

    @Override
    public String getMessageKey() {
        return "request.without-authorization";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return null;
    }

}

package br.com.caiqueborges.sprello.user.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@AllArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExistsException extends BaseException {

    private String userEmail;

    @Override
    public String getMessageKey() {
        return "user.exists";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Map.of("email", userEmail);
    }

}

package br.com.caiqueborges.sprello.config.controlleradvice;

import java.util.Map;

public abstract class BaseException extends RuntimeException {

    public abstract String getMessageKey();

    public abstract Map<String, Object> getMessageVariables();

}

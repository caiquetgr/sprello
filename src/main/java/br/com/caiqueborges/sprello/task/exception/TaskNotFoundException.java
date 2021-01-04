package br.com.caiqueborges.sprello.task.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends BaseException {

    private final Long boardId;

    public TaskNotFoundException(Long boardId) {
        this.boardId = boardId;
    }

    @Override
    public String getMessageKey() {
        return "task.notfound";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Map.of("id", boardId);
    }

}

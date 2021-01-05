package br.com.caiqueborges.sprello.task.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskStatusNotFoundException extends BaseException {

    private final Long taskStatusId;

    public TaskStatusNotFoundException(Long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    @Override
    public String getMessageKey() {
        return "taskstatus.notfound";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Map.of("id", taskStatusId);
    }

}

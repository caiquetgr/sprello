package br.com.caiqueborges.sprello.task.service;

import br.com.caiqueborges.sprello.task.repository.entity.Task;

import javax.validation.constraints.NotNull;

public interface UpdateTaskService {

    Task updateTaskStatus(@NotNull Long taskId, @NotNull Long boardId, @NotNull Long taskStatusId);

}

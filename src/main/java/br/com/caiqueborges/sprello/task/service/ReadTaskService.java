package br.com.caiqueborges.sprello.task.service;

import br.com.caiqueborges.sprello.task.repository.entity.Task;

import javax.validation.constraints.NotNull;

public interface ReadTaskService {

    Task getTaskByIdAndBoardId(@NotNull Long taskId, @NotNull Long boardId);

}

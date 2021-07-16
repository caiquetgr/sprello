package br.com.caiqueborges.sprello.task.service;

import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;

public interface ReadTaskStatusService {

    TaskStatus getTaskStatusToDo();

    TaskStatus findTaskStatusById(Long id);
}

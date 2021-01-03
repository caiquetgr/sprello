package br.com.caiqueborges.sprello.task.controller.mapper;

import br.com.caiqueborges.sprello.task.controller.model.CreateTaskRequest;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskResponse;
import br.com.caiqueborges.sprello.task.repository.entity.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskControllerMapper {

    Task createTaskRequestToTask(CreateTaskRequest request, Long boardId);

    CreateTaskResponse taskToCreateTaskResponse(Task task);

}

package br.com.caiqueborges.sprello.task.controller.mapper;

import br.com.caiqueborges.sprello.task.controller.model.CreateTaskRequest;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskResponse;
import br.com.caiqueborges.sprello.task.controller.model.GetTaskByIdResponse;
import br.com.caiqueborges.sprello.task.controller.model.UpdateTaskStatusResponse;
import br.com.caiqueborges.sprello.task.repository.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskControllerMapper {

    Task createTaskRequestToTask(CreateTaskRequest request, Long boardId);

    CreateTaskResponse taskToCreateTaskResponse(Task task);

    @Mapping(source = "task.createdById", target = "createdBy")
    @Mapping(source = "task.lastModifiedById", target = "lastModifiedBy")
    GetTaskByIdResponse taskToGetTaskByIdResponse(Task task);

    @Mapping(source = "task.createdById", target = "createdBy")
    @Mapping(source = "task.lastModifiedById", target = "lastModifiedBy")
    UpdateTaskStatusResponse taskToUpdateTaskStatusResponse(Task task);

}

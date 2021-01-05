package br.com.caiqueborges.sprello.task.controller;

import br.com.caiqueborges.sprello.task.controller.mapper.TaskControllerMapper;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskRequest;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskResponse;
import br.com.caiqueborges.sprello.task.controller.model.GetTaskByIdResponse;
import br.com.caiqueborges.sprello.task.controller.model.UpdateTaskStatusResponse;
import br.com.caiqueborges.sprello.task.service.CreateTaskService;
import br.com.caiqueborges.sprello.task.service.ReadTaskService;
import br.com.caiqueborges.sprello.task.service.UpdateTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TasksController {

    public static final String ENDPOINT_TASKS = "/boards/{boardId}/tasks";
    public static final String ENDPOINT_TASKS_BY_ID = ENDPOINT_TASKS + "/{id}";
    public static final String ENDPOINT_TASK_STATUS_BY_ID = ENDPOINT_TASKS_BY_ID + "/status/{taskStatusId}";

    private final CreateTaskService createTaskService;
    private final ReadTaskService readTaskService;
    private final UpdateTaskService updateTaskService;
    private final TaskControllerMapper taskControllerMapper;

    @PostMapping(ENDPOINT_TASKS)
    public ResponseEntity<CreateTaskResponse> createTask(@PathVariable("boardId") Long boardId,
                                                         @Valid @RequestBody CreateTaskRequest request) {

        return Optional.of(taskControllerMapper.createTaskRequestToTask(request, boardId))
                .map(createTaskService::createTask)
                .map(taskControllerMapper::taskToCreateTaskResponse)
                .map(this::buildResponseEntityCreatedWithBody)
                .orElseGet(() -> ResponseEntity.noContent().build());

    }

    private ResponseEntity<CreateTaskResponse> buildResponseEntityCreatedWithBody(CreateTaskResponse createTaskResponse) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createTaskResponse);
    }

    @GetMapping(ENDPOINT_TASKS_BY_ID)
    public ResponseEntity<GetTaskByIdResponse> getTaskById(@PathVariable("boardId") Long boardId,
                                                           @PathVariable("id") Long id) {

        return Optional.of(readTaskService.getTaskByIdAndBoardId(id, boardId))
                .map(taskControllerMapper::taskToGetTaskByIdResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());

    }

    @PatchMapping(ENDPOINT_TASK_STATUS_BY_ID)
    public ResponseEntity<UpdateTaskStatusResponse> updateTaskStatus(@PathVariable("boardId") Long boardId,
                                                                     @PathVariable("id") Long id,
                                                                     @PathVariable("taskStatusId") Long taskStatusId) {

        return Optional.of(updateTaskService.updateTaskStatus(id, boardId, taskStatusId))
                .map(taskControllerMapper::taskToUpdateTaskStatusResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());

    }

}

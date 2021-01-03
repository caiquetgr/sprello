package br.com.caiqueborges.sprello.task.controller;

import br.com.caiqueborges.sprello.task.controller.mapper.TaskControllerMapper;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskRequest;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskResponse;
import br.com.caiqueborges.sprello.task.service.CreateTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final CreateTaskService createTaskService;
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

}

package br.com.caiqueborges.sprello.task.service.impl;

import br.com.caiqueborges.sprello.task.exception.TaskStatusNotFoundException;
import br.com.caiqueborges.sprello.task.repository.TaskStatusRepository;
import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import br.com.caiqueborges.sprello.task.service.ReadTaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class TaskStatusService implements ReadTaskStatusService {

    private static final Long TASK_STATUS_TO_DO_ID = 1L;

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus getTaskStatusToDo() {
        return taskStatusRepository.findById(TASK_STATUS_TO_DO_ID)
                .orElseThrow(() -> new TaskStatusNotFoundException(TASK_STATUS_TO_DO_ID));
    }

}

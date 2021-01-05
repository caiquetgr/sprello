package br.com.caiqueborges.sprello.task.service.impl;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.ReadBoardService;
import br.com.caiqueborges.sprello.task.exception.TaskNotFoundException;
import br.com.caiqueborges.sprello.task.repository.TaskRepository;
import br.com.caiqueborges.sprello.task.repository.entity.Task;
import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import br.com.caiqueborges.sprello.task.service.CreateTaskService;
import br.com.caiqueborges.sprello.task.service.ReadTaskService;
import br.com.caiqueborges.sprello.task.service.ReadTaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Service
class TaskService implements CreateTaskService, ReadTaskService {

    private final ReadBoardService readBoardService;
    private final ReadTaskStatusService readTaskStatusService;
    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {

        final Board board = readBoardService.getBoardById(task.getBoardId());
        final TaskStatus taskStatusToDo = readTaskStatusService.getTaskStatusToDo();

        task.setBoard(board);
        task.setTaskStatus(taskStatusToDo);

        return taskRepository.save(task);

    }

    @Override
    public Task getTaskByIdAndBoardId(@NotNull Long taskId, @NotNull Long boardId) {

        final Board board = readBoardService.getBoardById(boardId);

        return taskRepository.findByIdAndBoardIdAndDeletedFalse(taskId, board.getId())
                .orElseThrow(() -> new TaskNotFoundException(taskId));

    }

}

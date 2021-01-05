package br.com.caiqueborges.sprello.task.repository;

import br.com.caiqueborges.sprello.base.repository.UserAuditedRepository;
import br.com.caiqueborges.sprello.task.repository.entity.Task;

import java.util.Optional;

public interface TaskRepository extends UserAuditedRepository<Task, Long> {

    Optional<Task> findByIdAndBoardIdAndDeletedFalse(Long taskId, Long boardId);

}

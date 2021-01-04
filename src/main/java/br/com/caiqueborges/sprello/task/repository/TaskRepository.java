package br.com.caiqueborges.sprello.task.repository;

import br.com.caiqueborges.sprello.base.repository.UserAuditedRepository;
import br.com.caiqueborges.sprello.task.repository.entity.Task;

public interface TaskRepository extends UserAuditedRepository<Task, Long> {
}

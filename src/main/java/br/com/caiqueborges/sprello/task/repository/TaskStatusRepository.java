package br.com.caiqueborges.sprello.task.repository;

import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}

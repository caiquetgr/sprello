package br.com.caiqueborges.sprello.task.repository;

import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TaskStatusRepositoryTest {

    @Autowired
    private TaskStatusRepository repository;

    @Test
    void whenFindByIdToDoTask_thenReturnTaskStatus() {

        final long taskStatusToDoId = 1L;
        final TaskStatus taskStatus = repository.findById(taskStatusToDoId).orElse(null);

        failIfNull(taskStatus);

        assertThat(taskStatus.getId()).isEqualTo(taskStatusToDoId);
        assertThat(taskStatus.getName()).isEqualTo("To do");
        assertThat(taskStatus.getDescription()).isEqualTo("Task is awaiting to be started");

    }

    @Test
    void whenFindByIdProgressTask_thenReturnTaskStatus() {

        final long taskStatusProgressId = 2L;
        final TaskStatus taskStatus = repository.findById(taskStatusProgressId).orElse(null);

        failIfNull(taskStatus);

        assertThat(taskStatus.getId()).isEqualTo(taskStatusProgressId);
        assertThat(taskStatus.getName()).isEqualTo("Progress");
        assertThat(taskStatus.getDescription()).isEqualTo("Task is in progress");

    }

    @Test
    void whenFindByIdRevisionTask_thenReturnTaskStatus() {

        final long taskStatusRevisionId = 3L;
        final TaskStatus taskStatus = repository.findById(taskStatusRevisionId).orElse(null);

        failIfNull(taskStatus);

        assertThat(taskStatus.getId()).isEqualTo(taskStatusRevisionId);
        assertThat(taskStatus.getName()).isEqualTo("Revision");
        assertThat(taskStatus.getDescription()).isEqualTo("Task in done but still needs to be revised");

    }

    @Test
    void whenFindByIdDoneTask_thenReturnTaskStatus() {

        final long taskStatusDoneId = 4L;
        final TaskStatus taskStatus = repository.findById(taskStatusDoneId).orElse(null);

        failIfNull(taskStatus);

        assertThat(taskStatus.getId()).isEqualTo(taskStatusDoneId);
        assertThat(taskStatus.getName()).isEqualTo("Done");
        assertThat(taskStatus.getDescription()).isEqualTo("Task has already been finished");

    }

    @Test
    void whenFindByIdCancelledTask_thenReturnTaskStatus() {

        final long taskStatusCancelledId = 5L;
        final TaskStatus taskStatus = repository.findById(taskStatusCancelledId).orElse(null);

        failIfNull(taskStatus);

        assertThat(taskStatus.getId()).isEqualTo(taskStatusCancelledId);
        assertThat(taskStatus.getName()).isEqualTo("Cancelled");
        assertThat(taskStatus.getDescription()).isEqualTo("Task has been cancelled");

    }

    private void failIfNull(TaskStatus taskStatus) {

        if (Objects.isNull(taskStatus)) {
            fail("Task status returned null");
        }

    }

}

package br.com.caiqueborges.sprello.task.service.impl;

import br.com.caiqueborges.sprello.task.exception.TaskNotFoundException;
import br.com.caiqueborges.sprello.task.fixture.TaskStatusTemplateLoader;
import br.com.caiqueborges.sprello.task.repository.TaskStatusRepository;
import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static br.com.caiqueborges.sprello.util.TestUtils.loadFixture;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskStatusServiceTest {

    @Mock
    private TaskStatusRepository taskStatusRepository;

    @InjectMocks
    private TaskStatusService service;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.task.fixture");
    }

    @Test
    void whenGetTaskStatusToDo_thenReturnTaskStatusToDo() {

        final TaskStatus taskStatusToDo = loadFixture(TaskStatusTemplateLoader.TASK_STATUS_TO_DO, TaskStatus.class);

        given(taskStatusRepository.findById(1L))
                .willReturn(Optional.of(taskStatusToDo));

        final TaskStatus taskStatusReturned = service.getTaskStatusToDo();

        assertThat(taskStatusReturned).isNotNull();
        assertThat(taskStatusReturned.getId()).isEqualTo(1L);
        assertThat(taskStatusReturned.getName()).isEqualTo("To do");
        assertThat(taskStatusReturned.getDescription()).isEqualTo("Task is awaiting to be started");

        verify(taskStatusRepository).findById(1L);

    }

    @Test
    void whenGetTaskStatusToDo_andTaskStatusDontExists_thenThrowTaskNotFoundException() {

        given(taskStatusRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTaskStatusToDo())
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskStatusRepository).findById(1L);

    }

}

package br.com.caiqueborges.sprello.task.service.impl;

import br.com.caiqueborges.sprello.board.fixture.BoardTemplateLoader;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.ReadBoardService;
import br.com.caiqueborges.sprello.task.fixture.TaskStatusTemplateLoader;
import br.com.caiqueborges.sprello.task.fixture.TaskTemplateLoader;
import br.com.caiqueborges.sprello.task.repository.entity.Task;
import br.com.caiqueborges.sprello.task.repository.entity.TaskRepository;
import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import br.com.caiqueborges.sprello.task.service.ReadTaskStatusService;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.caiqueborges.sprello.util.TestUtils.loadFixture;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Tag("task")
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private ReadBoardService readBoardService;

    @Mock
    private ReadTaskStatusService readTaskStatusService;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.board.fixture");
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.task.fixture");
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @Tag("create_task")
    @Test
    void whenCreateTask_thenReturnTask() {

        final Board board = loadFixture(BoardTemplateLoader.AFTER_INSERT, Board.class);
        final Task task = loadFixture(TaskTemplateLoader.PRE_INSERT, Task.class);
        final TaskStatus taskStatusToDo = loadFixture(TaskStatusTemplateLoader.TASK_STATUS_TO_DO, TaskStatus.class);

        given(readBoardService.getBoardById(board.getId()))
                .willReturn(board);

        given(readTaskStatusService.getTaskStatusToDo())
                .willReturn(taskStatusToDo);

        given(taskRepository.save(task))
                .willReturn(loadFixture(TaskTemplateLoader.AFTER_INSERT, Task.class));

        final Task taskReturned = taskService.createTask(task);

        assertThat(taskReturned).isNotNull();
        assertThat(taskReturned.getId()).isEqualTo(1L);
        assertThat(taskReturned.getBoardId()).isEqualTo(1L);
        assertThat(taskReturned.getBoard()).isEqualTo(board);
        assertThat(taskReturned.getName()).isEqualTo("New task");
        assertThat(taskReturned.getDescription()).isEqualTo("The new task description");
        assertThat(taskReturned.getTaskStatus()).isEqualTo(taskStatusToDo);

        verify(readBoardService).getBoardById(board.getId());
        verify(readTaskStatusService).getTaskStatusToDo();
        verify(taskRepository).save(taskCaptor.capture());

        final Task taskCaptured = taskCaptor.getValue();

        assertThat(taskCaptured.getBoardId()).isEqualTo(1L);
        assertThat(taskCaptured.getBoard()).isEqualTo(board);
        assertThat(taskCaptured.getName()).isEqualTo("New task");
        assertThat(taskCaptured.getDescription()).isEqualTo("The new task description");
        assertThat(taskCaptured.getTaskStatus()).isEqualTo(taskStatusToDo);

    }

    // TODO: create task error scenarios

}

package br.com.caiqueborges.sprello.task.controller;

import br.com.caiqueborges.sprello.base.BaseTestController;
import br.com.caiqueborges.sprello.task.controller.mapper.TaskControllerMapperImpl;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskRequest;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskResponse;
import br.com.caiqueborges.sprello.task.controller.model.TaskStatusResponse;
import br.com.caiqueborges.sprello.task.repository.entity.Task;
import br.com.caiqueborges.sprello.task.service.CreateTaskService;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;

import static br.com.caiqueborges.sprello.task.fixture.TaskTemplateLoader.CREATE_TASK_REQUEST_VALID;
import static br.com.caiqueborges.sprello.task.fixture.TaskTemplateLoader.CREATE_TASK_REQUEST_VALID_ENTITY_RETURN;
import static br.com.caiqueborges.sprello.util.TestUtils.loadFixture;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("task")
@WebMvcTest(TasksController.class)
class TasksControllerTest extends BaseTestController {

    @MockBean
    private CreateTaskService createTaskService;

    @SpyBean
    private TaskControllerMapperImpl taskControllerMapper;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.task.fixture");
    }

    @SneakyThrows
    @Tag("create_task")
    @Test
    void whenCreateTask_thenReturnStatus201AndCreateBoardResponse() {

        final Long boardId = 1L;
        final CreateTaskRequest createBoardRequest = loadFixture(CREATE_TASK_REQUEST_VALID, CreateTaskRequest.class);
        final Task taskCreated = loadFixture(CREATE_TASK_REQUEST_VALID_ENTITY_RETURN, Task.class);

        given(createTaskService.createTask(any(Task.class)))
                .willReturn(taskCreated);

        final String responseBody = mockMvc.perform(post(TasksController.ENDPOINT_TASKS, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final CreateTaskResponse createTaskResponse = objectMapper.readValue(responseBody, CreateTaskResponse.class);

        assertThat(createTaskResponse.getId()).isEqualTo(1L);
        assertThat(createTaskResponse.getBoardId()).isEqualTo(1L);
        assertThat(createTaskResponse.getName()).isEqualTo("New task");
        assertThat(createTaskResponse.getDescription()).isEqualTo("The new task description");
        assertThat(createTaskResponse.getTaskStatus()).isEqualToComparingFieldByField(expectedTaskStatusTodo());

        verify(createTaskService).createTask(taskCaptor.capture());

        final Task taskCaptured = taskCaptor.getValue();

        assertThat(taskCaptured.getBoardId()).isEqualTo(1L);
        assertThat(taskCaptured.getName()).isEqualTo(createBoardRequest.getName());
        assertThat(taskCaptured.getDescription()).isEqualTo(createBoardRequest.getDescription());

    }

    private TaskStatusResponse expectedTaskStatusTodo() {
        return TaskStatusResponse.builder()
                .id(1L)
                .name("To do")
                .description("Task is awaiting to be started")
                .build();
    }

    @SneakyThrows
    @Tag("create_task")
    @Test
    void whenCreateTask_andNameIsEmpty_thenReturn400() {

        final Long boardId = 1L;
        final CreateTaskRequest createBoardRequest = loadFixture(CREATE_TASK_REQUEST_VALID, CreateTaskRequest.class);

        createBoardRequest.setName(StringUtils.EMPTY);

        mockMvc.perform(post(TasksController.ENDPOINT_TASKS, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Tag("create_task")
    @Test
    void whenCreateTask_andNameIsNull_thenReturn400() {

        final Long boardId = 1L;
        final CreateTaskRequest createBoardRequest = loadFixture(CREATE_TASK_REQUEST_VALID, CreateTaskRequest.class);

        createBoardRequest.setName(null);

        mockMvc.perform(post(TasksController.ENDPOINT_TASKS, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Tag("create_task")
    @Test
    void whenCreateTask_andNameSizeGreaterThanAllowed_thenReturn400() {

        final Long boardId = 1L;
        final CreateTaskRequest createBoardRequest = loadFixture(CREATE_TASK_REQUEST_VALID, CreateTaskRequest.class);

        createBoardRequest.setName(RandomStringUtils.random(101));

        mockMvc.perform(post(TasksController.ENDPOINT_TASKS, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Tag("create_task")
    @Test
    void whenCreateTask_andDescriptionIsEmpty_thenReturn400() {

        final Long boardId = 1L;
        final CreateTaskRequest createBoardRequest = loadFixture(CREATE_TASK_REQUEST_VALID, CreateTaskRequest.class);

        createBoardRequest.setDescription(StringUtils.EMPTY);

        mockMvc.perform(post(TasksController.ENDPOINT_TASKS, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Tag("create_task")
    @Test
    void whenCreateTask_andDescriptionIsNull_thenReturn400() {

        final Long boardId = 1L;
        final CreateTaskRequest createBoardRequest = loadFixture(CREATE_TASK_REQUEST_VALID, CreateTaskRequest.class);

        createBoardRequest.setDescription(null);

        mockMvc.perform(post(TasksController.ENDPOINT_TASKS, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Tag("create_task")
    @Test
    void whenCreateTask_andDescriptionSizeGreaterThanAllowed_thenReturn400() {

        final Long boardId = 1L;
        final CreateTaskRequest createBoardRequest = loadFixture(CREATE_TASK_REQUEST_VALID, CreateTaskRequest.class);

        createBoardRequest.setDescription(RandomStringUtils.random(501));

        mockMvc.perform(post(TasksController.ENDPOINT_TASKS, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isBadRequest());

    }

}

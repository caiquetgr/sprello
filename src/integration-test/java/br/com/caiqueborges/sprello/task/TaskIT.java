package br.com.caiqueborges.sprello.task;

import br.com.caiqueborges.sprello.AuthenticatedIT;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static br.com.caiqueborges.sprello.board.BoardIT.INSERT_BOARD_1_DELETED_SQL;
import static br.com.caiqueborges.sprello.board.BoardIT.INSERT_BOARD_1_SQL;
import static br.com.caiqueborges.sprello.login.LoginIT.CREATE_VALID_USER_SQL;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("task")
public class TaskIT extends AuthenticatedIT {

    private static final String ENDPOINT_TASKS = "/boards/{boardId}/tasks";
    private static final String ENDPOINT_TASKS_BY_ID = "/boards/{boardId}/tasks/{id}";
    private static final String ENDPOINT_UPDATE_TASK_STATUS_BY_ID = ENDPOINT_TASKS_BY_ID + "/status/{statusId}";

    private static final String TASKS_JSON_RESOURCE = JSON_FOLDER + "task/";
    private static final String CREATE_TASK_VALID_REQUEST_JSON = TASKS_JSON_RESOURCE + "create-task-valid-request.json";
    private static final String CREATE_TASK_VALID_RESPONSE_JSON = TASKS_JSON_RESOURCE + "create-task-valid-response.json";

    private static final String GET_BOARD_BY_ID_RESPONSE_JSON = TASKS_JSON_RESOURCE + "get-task-by-id-response.json";

    private static final String TASK_SQL_RESOURCE = SQL_FOLDER + "task/";
    private static final String INSERT_TASK_1_TODO_SQL = TASK_SQL_RESOURCE + "insert-task-1-todo.sql";
    private static final String INSERT_TASK_1_TODO_DELETED_SQL = TASK_SQL_RESOURCE + "insert-task-1-todo-deleted.sql";

    private static final String UPDATE_TASK_STATUS_1_TODO = TASK_SQL_RESOURCE + "update-task-status-1-todo.json";
    private static final String UPDATE_TASK_STATUS_2_PROGRESS = TASK_SQL_RESOURCE + "update-task-status-2-progress.json";
    private static final String UPDATE_TASK_STATUS_3_REVISION = TASK_SQL_RESOURCE + "update-task-status-3-revision.json";
    private static final String UPDATE_TASK_STATUS_4_DONE = TASK_SQL_RESOURCE + "update-task-status-4-done.json";
    private static final String UPDATE_TASK_STATUS_5_CANCELLED = TASK_SQL_RESOURCE + "update-task-status-5-cancelled.json";

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andValid_thenReturn201AndCreateTaskResponse() {

        final Long boardId = 1L;

        this.performAuthenticated(post(ENDPOINT_TASKS, boardId)
                .content(readFile(CREATE_TASK_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonIsEqualToFile(CREATE_TASK_VALID_RESPONSE_JSON));

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andBoardNotFound_thenReturn404() {

        final Long boardId = 1L;

        this.performAuthenticated(post(ENDPOINT_TASKS, boardId)
                .content(readFile(CREATE_TASK_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_DELETED_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andBoardIsDeleted_thenReturn404() {

        final Long boardId = 1L;

        this.performAuthenticated(post(ENDPOINT_TASKS, boardId)
                .content(readFile(CREATE_TASK_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andWithoutAuthentication_thenReturn401() {

        final Long boardId = 1L;

        this.mockMvc.perform(post(ENDPOINT_TASKS, boardId)
                .content(readFile(CREATE_TASK_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("get_task_by_id")
    @Test
    void whenGetTaskById_thenReturn200AndGetTaskByIdResponse() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        this.performAuthenticated(get(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(GET_BOARD_BY_ID_RESPONSE_JSON));

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL})
    @Tag("get_task_by_id")
    @Test
    void whenGetTaskById_andTaskNotFound_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        this.performAuthenticated(get(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_DELETED_SQL})
    @Tag("get_task_by_id")
    @Test
    void whenGetTaskById_andTaskIsDeleted_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        this.performAuthenticated(get(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("get_task_by_id")
    @Test
    void whenGetTaskById_andTaskDoesNotBelongToBoardIdFromRequest_thenReturn404() {

        final Long boardId = 99L;
        final Long taskId = 1L;

        this.performAuthenticated(get(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("get_task_by_id")
    @Test
    void whenGetTaskById_andBoardNotFound_thenReturn404() {

        final Long boardId = 99L;
        final Long taskId = 1L;

        this.performAuthenticated(get(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_DELETED_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("get_task_by_id")
    @Test
    void whenGetTaskById_andBoardIsDeleted_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        this.performAuthenticated(get(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("get_task_by_id")
    @Test
    void whenGetTaskById_andWithoutAuthentication_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        this.mockMvc.perform(get(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("update_task_status")
    @MethodSource("updateTaskToStatusArguments")
    @ParameterizedTest
    void whenUpdateTaskStatus_thenReturn200AndUpdateTaskStatusByIdResponse(Long taskStatusId,
                                                                           String jsonfileExpected) {

        final Long boardId = 1L;
        final Long taskId = 1L;

        this.performAuthenticated(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(jsonfileExpected));

    }

    private static Stream<Arguments> updateTaskToStatusArguments() {
        return Stream.of(
                arguments(1L, UPDATE_TASK_STATUS_1_TODO),
                arguments(2L, UPDATE_TASK_STATUS_2_PROGRESS),
                arguments(3L, UPDATE_TASK_STATUS_3_REVISION),
                arguments(4L, UPDATE_TASK_STATUS_4_DONE),
                arguments(5L, UPDATE_TASK_STATUS_5_CANCELLED)
        );
    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL})
    @Tag("update_task_status")
    @Test
    void whenUpdateTaskStatus_andTaskNotFound_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        final Long taskStatusId = 1L;

        this.performAuthenticated(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_DELETED_SQL})
    @Tag("update_task_status")
    @Test
    void whenUpdateTaskStatus_andTaskIsDeleted_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        final Long taskStatusId = 1L;

        this.performAuthenticated(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("update_task_status")
    @Test
    void whenUpdateTaskStatus_andTaskDoesNotBelongToBoardIdFromRequest_thenReturn404() {

        final Long boardId = 99L;
        final Long taskId = 1L;

        final Long taskStatusId = 1L;

        this.performAuthenticated(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_DELETED_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("update_task_status")
    @Test
    void whenUpdateTaskStatus_andBoardIsDeleted_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        final Long taskStatusId = 1L;

        this.performAuthenticated(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("update_task_status")
    @Test
    void whenUpdateTaskStatus_andBoardIsNotFound_thenReturn404() {

        final Long boardId = 99L;
        final Long taskId = 1L;

        final Long taskStatusId = 1L;

        this.performAuthenticated(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("update_task_status")
    @Test
    void whenUpdateTaskStatus_andTaskStatusIsNotFound_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        final Long taskStatusId = 99L;

        this.performAuthenticated(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL, INSERT_TASK_1_TODO_SQL})
    @Tag("update_task_status")
    @Test
    void whenUpdateTaskStatus_andWithoutAuthentication_thenReturn404() {

        final Long boardId = 1L;
        final Long taskId = 1L;

        final Long taskStatusId = 1L;

        this.mockMvc.perform(patch(ENDPOINT_UPDATE_TASK_STATUS_BY_ID, boardId, taskId, taskStatusId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

}

package br.com.caiqueborges.sprello.task;

import br.com.caiqueborges.sprello.AuthenticatedIT;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static br.com.caiqueborges.sprello.board.BoardIT.INSERT_BOARD_1_DELETED_SQL;
import static br.com.caiqueborges.sprello.board.BoardIT.INSERT_BOARD_1_SQL;
import static br.com.caiqueborges.sprello.login.LoginIT.CREATE_VALID_USER_SQL;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("task")
public class TaskIT extends AuthenticatedIT {

    private static final String ENDPOINT_TASKS = "/boards/{boardId}/tasks";
    private static final String ENDPOINT_TASKS_BY_ID = "/boards/{boardId}/tasks/{id}";

    private static final String TASKS_JSON_RESOURCE = JSON_FOLDER + "task/";
    private static final String CREATE_TASK_VALID_REQUEST_JSON = TASKS_JSON_RESOURCE + "create-task-valid-request.json";
    private static final String CREATE_TASK_VALID_RESPONSE_JSON = TASKS_JSON_RESOURCE + "create-task-valid-response.json";

    private static final String GET_BOARD_BY_ID_RESPONSE_JSON = TASKS_JSON_RESOURCE + "get-task-by-id-response.json";

    private static final String TASK_SQL_RESOURCE = SQL_FOLDER + "task/";
    private static final String INSERT_TASK_1_TODO_SQL = TASK_SQL_RESOURCE + "insert-task-1-todo.sql";
    private static final String INSERT_TASK_1_TODO_DELETED_SQL = TASK_SQL_RESOURCE + "insert-task-1-todo-deleted.sql";


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

        this.mockMvc.perform(post(ENDPOINT_TASKS_BY_ID, boardId, taskId)
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

        this.mockMvc.perform(post(ENDPOINT_TASKS_BY_ID, boardId, taskId)
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

        this.mockMvc.perform(post(ENDPOINT_TASKS_BY_ID, boardId, taskId)
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

        this.mockMvc.perform(post(ENDPOINT_TASKS_BY_ID, boardId, taskId)
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

        this.mockMvc.perform(post(ENDPOINT_TASKS_BY_ID, boardId, taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}

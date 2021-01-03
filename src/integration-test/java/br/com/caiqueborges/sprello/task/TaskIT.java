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

    private static final String TASKS_JSON_RESOURCE = JSON_FOLDER + "task/";
    private static final String CREATE_TASK_VALID_REQUEST_JSON = TASKS_JSON_RESOURCE + "create-task-valid-request.json";
    private static final String CREATE_TASK_VALID_RESPONSE_JSON = TASKS_JSON_RESOURCE + "create-task-valid-response.json";

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andValid_thenReturn201AndCreateTaskResponse() {

        this.performAuthenticated(post(ENDPOINT_TASKS)
                .content(CREATE_TASK_VALID_REQUEST_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonIsEqualToFile(CREATE_TASK_VALID_RESPONSE_JSON));

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andBoardNotFound_thenReturn404() {

        this.performAuthenticated(post(ENDPOINT_TASKS)
                .content(CREATE_TASK_VALID_REQUEST_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_DELETED_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andBoardIsDeleted_thenReturn404() {

        this.mockMvc.perform(post(ENDPOINT_TASKS)
                .content(CREATE_TASK_VALID_REQUEST_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Sql(scripts = {CREATE_VALID_USER_SQL})
    @Tag("create_task")
    @Test
    void whenCreateTask_andWithoutAuthentication_thenReturn401() {

        this.mockMvc.perform(post(ENDPOINT_TASKS)
                .content(CREATE_TASK_VALID_REQUEST_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

}

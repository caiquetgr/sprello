package br.com.caiqueborges.sprello.board;

import br.com.caiqueborges.sprello.AuthenticatedIT;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;

import static br.com.caiqueborges.sprello.board.controller.BoardController.ENDPOINT_BOARDS_BY_ID;
import static br.com.caiqueborges.sprello.login.LoginIT.CREATE_VALID_USER_2_SQL;
import static br.com.caiqueborges.sprello.login.LoginIT.CREATE_VALID_USER_SQL;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardIT extends AuthenticatedIT {

    private static final String BOARDS_ENDPOINT = "/boards";

    private static final String BOARDS_JSON_RESOURCE = JSON_FOLDER + "board/";

    private static final String CREATE_BOARD_VALID_REQUEST_JSON = BOARDS_JSON_RESOURCE + "create-board-valid-request.json";
    private static final String CREATE_BOARD_VALID_RESPONSE_JSON = BOARDS_JSON_RESOURCE + "create-board-valid-response.json";
    private static final String CREATE_BOARD_INVALID_REQUEST_JSON = BOARDS_JSON_RESOURCE + "create-board-invalid-request.json";

    private static final String GET_BOARD_BY_ID_RESPONSE_JSON = BOARDS_JSON_RESOURCE + "get-board-by-id-response.json";

    private static final String UPDATE_BOARD_VALID_REQUEST_JSON = BOARDS_JSON_RESOURCE + "update-board-valid-request.json";
    private static final String UPDATE_BOARD_VALID_RESPONSE_JSON = BOARDS_JSON_RESOURCE + "update-board-valid-response.json";

    private static final String BOARDS_SQL_RESOURCE = SQL_FOLDER + "board/";

    public static final String INSERT_BOARD_1_SQL = BOARDS_SQL_RESOURCE + "insert-board-1.sql";
    private static final String INSERT_BOARD_2_SQL = BOARDS_SQL_RESOURCE + "insert-board-2.sql";
    public static final String INSERT_BOARD_1_DELETED_SQL = BOARDS_SQL_RESOURCE + "insert-board-1-deleted.sql";

    @Autowired
    private EntityManager entityManager;

    @SneakyThrows
    @Test
    void whenCreateBoard_thenReturn201AndCreatedEntity() {

        this.performAuthenticated(post(BOARDS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(CREATE_BOARD_VALID_REQUEST_JSON)))
                .andExpect(status().isCreated())
                .andExpect(jsonIsEqualToFile(CREATE_BOARD_VALID_RESPONSE_JSON));

    }

    @SneakyThrows
    @Test
    void whenCreateBoard_withoutAuthentication_thenReturn401() {

        this.mockMvc.perform(post(BOARDS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(CREATE_BOARD_VALID_REQUEST_JSON)))
                .andExpect(status().isUnauthorized());

    }

    @SneakyThrows
    @Test
    void whenCreateBoard_withNameEmpty_thenReturn400() {

        performAuthenticated(post(BOARDS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(CREATE_BOARD_INVALID_REQUEST_JSON)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"The field \\\"name\\\" can't be empty\"")));

    }

    @SneakyThrows
    @Test
    void whenCreateBoard_withContentNull_thenReturn400() {

        performAuthenticated(post(BOARDS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL})
    @SneakyThrows
    @Test
    void whenGetBoardById_andBoardExists_thenReturn200AndGetBoardByIdResponse() {

        final Long boardId = 1L;

        performAuthenticated(get(ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(GET_BOARD_BY_ID_RESPONSE_JSON));

    }

    @SneakyThrows
    @Test
    void whenGetBoardById_withoutAuthentication_thenReturn401() {

        final Long boardId = 1L;

        this.mockMvc.perform(get(ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_DELETED_SQL})
    @SneakyThrows
    @Test
    void whenGetBoardById_andBoardIsDeleted_thenReturn404() {

        final Long boardId = 1L;

        performAuthenticated(get(ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL})
    @SneakyThrows
    @Test
    void whenGetBoardById_andBoardDoesNotExists_thenReturn404() {

        final Long boardId = 2L;

        performAuthenticated(get(ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL, CREATE_VALID_USER_2_SQL, INSERT_BOARD_2_SQL})
    @SneakyThrows
    @Test
    void whenGetBoardById_andBoardExists_andItIsFromAnotherUser_thenReturn404() {

        final Long boardId = 2L;

        performAuthenticated(get(ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL})
    @SneakyThrows
    @Test
    void whenDeleteBoardById_andBoardExists_thenReturn204() {

        final Long boardId = 1L;

        performAuthenticated(delete(ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        final Board board = entityManager.find(Board.class, boardId);

        assertThat(board).isNotNull();
        assertThat(board.getDeleted()).isTrue();

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL, CREATE_VALID_USER_2_SQL, INSERT_BOARD_2_SQL})
    @SneakyThrows
    @Test
    void whenDeleteBoardById_andBoardExists_andItIsFromAnotherUser_thenReturn404() {

        final Long boardId = 2L;

        performAuthenticated(delete(ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_SQL})
    @SneakyThrows
    @Test
    void whenUpdateBoardById_andBoardExists_thenReturn200AndUpdateBoardResponse() {

        final Long boardId = 1L;

        performAuthenticated(put(ENDPOINT_BOARDS_BY_ID, boardId)
                .content(readFile(UPDATE_BOARD_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(UPDATE_BOARD_VALID_RESPONSE_JSON));

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL})
    @SneakyThrows
    @Test
    void whenUpdateBoardById_andBoardDoesNotExists_thenReturn404() {

        final Long boardId = 2L;

        performAuthenticated(put(ENDPOINT_BOARDS_BY_ID, boardId)
                .content(readFile(UPDATE_BOARD_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Sql(scripts = {CREATE_VALID_USER_SQL, INSERT_BOARD_1_DELETED_SQL})
    @SneakyThrows
    @Test
    void whenUpdateBoardById_andBoardIsDeleted_thenReturn404() {

        final Long boardId = 1L;

        performAuthenticated(put(ENDPOINT_BOARDS_BY_ID, boardId)
                .content(readFile(UPDATE_BOARD_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Sql(scripts = {CREATE_VALID_USER_SQL, CREATE_VALID_USER_2_SQL, INSERT_BOARD_2_SQL})
    @SneakyThrows
    @Test
    void whenUpdateBoardById_andBoardExists_andItIsFromAnotherUser_thenReturn404() {

        final Long boardId = 2L;

        performAuthenticated(put(ENDPOINT_BOARDS_BY_ID, boardId)
                .content(readFile(UPDATE_BOARD_VALID_REQUEST_JSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}

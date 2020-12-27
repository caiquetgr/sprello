package br.com.caiqueborges.sprello.board;

import br.com.caiqueborges.sprello.AuthenticatedIT;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;

import static br.com.caiqueborges.sprello.board.controller.BoardController.ENDPOINT_BOARDS_BY_ID;
import static br.com.caiqueborges.sprello.security.LoginIT.CREATE_VALID_USER_SQL;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardIT extends AuthenticatedIT {

    private static final String BOARDS_ENDPOINT = "/boards";

    private static final String BOARDS_JSON_RESOURCE = JSON_FOLDER + "board/";
    private static final String CREATE_BOARD_VALID_REQUEST_JSON = BOARDS_JSON_RESOURCE + "create-board-valid-request.json";
    private static final String CREATE_BOARD_VALID_RESPONSE_JSON = BOARDS_JSON_RESOURCE + "create-board-valid-response.json";
    private static final String CREATE_BOARD_INVALID_REQUEST_JSON = BOARDS_JSON_RESOURCE + "create-board-invalid-request.json";
    private static final String GET_BOARD_BY_ID_RESPONSE_JSON = BOARDS_JSON_RESOURCE + "get-board-by-id-response.json";

    private static final String BOARDS_SQL_RESOURCE = SQL_FOLDER + "board/";
    private static final String INSERT_BOARD_1_SQL = BOARDS_SQL_RESOURCE + "insert-board-1.sql";

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
    void whenGetBoardById_andBoardExists_thenReturn200AndBoardResponse() {

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

}

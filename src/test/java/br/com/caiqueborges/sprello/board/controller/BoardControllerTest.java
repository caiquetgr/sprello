package br.com.caiqueborges.sprello.board.controller;

import br.com.caiqueborges.sprello.board.controller.mapper.BoardControllerMapperImpl;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardRequest;
import br.com.caiqueborges.sprello.board.fixture.BoardTemplateLoader;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.CreateBoardService;
import br.com.caiqueborges.sprello.board.service.ReadBoardService;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.caiqueborges.sprello.util.JsonUnitUtils.JSON_FOLDER;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    private static final String BOARD_JSON_FOLDER = JSON_FOLDER + "board/controller/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateBoardService createBoardService;

    @MockBean
    private ReadBoardService readBoardService;

    @SpyBean
    private BoardControllerMapperImpl boardControllerMapper;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.board.fixture");
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @SneakyThrows
    @Test
    void whenCreateBoard_thenReturnStatus201AndBodyEntity() {

        given(createBoardService.createBoard(any(Board.class)))
                .willReturn(new Board(1L, "Test board"));

        mockMvc.perform(post(BoardController.ENDPOINT_BOARDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateBoardRequest("Test board"))))
                .andExpect(status().isCreated())
                .andExpect(jsonIsEqualToFile(BOARD_JSON_FOLDER + "create-board-response.json"));

    }

    @SneakyThrows
    @Test
    void whenGetBoardById_thenReturnBoardResponse() {

        final Long boardId = 1L;

        given(readBoardService.getBoardById(boardId))
                .willReturn(Fixture.from(Board.class).gimme(BoardTemplateLoader.AFTER_INSERT));

        mockMvc.perform(get(BoardController.ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(BOARD_JSON_FOLDER + "get-board-by-id-response.json"));

    }

}

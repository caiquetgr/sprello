package br.com.caiqueborges.sprello.board.controller;

import br.com.caiqueborges.sprello.board.controller.mapper.BoardControllerMapperImpl;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardRequest;
import br.com.caiqueborges.sprello.board.controller.model.UpdateBoardRequest;
import br.com.caiqueborges.sprello.board.fixture.BoardTemplateLoader;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.CreateBoardService;
import br.com.caiqueborges.sprello.board.service.DeleteBoardService;
import br.com.caiqueborges.sprello.board.service.ReadBoardService;
import br.com.caiqueborges.sprello.board.service.UpdateBoardService;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.caiqueborges.sprello.util.JsonUnitUtils.JSON_FOLDER;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    private static final String BOARD_JSON_FOLDER = JSON_FOLDER + "board/controller/";

    private static final String GET_BOARD_BY_ID_RESPONSE_JSON = BOARD_JSON_FOLDER + "get-board-by-id-response.json";
    private static final String CREATE_BOARD_RESPONSE_JSON = BOARD_JSON_FOLDER + "create-board-response.json";
    private static final String UPDATE_BOARD_RESPONSE_JSON = BOARD_JSON_FOLDER + "update-board-response.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateBoardService createBoardService;

    @MockBean
    private ReadBoardService readBoardService;

    @MockBean
    private DeleteBoardService deleteBoardService;

    @MockBean
    private UpdateBoardService updateBoardService;

    @SpyBean
    private BoardControllerMapperImpl boardControllerMapper;

    @Captor
    private ArgumentCaptor<Board> boardCaptor;

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
                .andExpect(jsonIsEqualToFile(CREATE_BOARD_RESPONSE_JSON));

    }

    @SneakyThrows
    @Test
    void whenGetBoardById_thenReturn200AndBoardResponse() {

        final Long boardId = 1L;

        given(readBoardService.getBoardById(boardId))
                .willReturn(Fixture.from(Board.class).gimme(BoardTemplateLoader.AFTER_INSERT));

        mockMvc.perform(get(BoardController.ENDPOINT_BOARDS_BY_ID, boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(GET_BOARD_BY_ID_RESPONSE_JSON));

    }

    @SneakyThrows
    @Test
    void whenDeleteBoardById_thenReturn204() {

        final Long boardId = 1L;

        willDoNothing()
                .given(deleteBoardService)
                .deleteBoardById(boardId);

        mockMvc.perform(delete(BoardController.ENDPOINT_BOARDS_BY_ID, boardId))
                .andExpect(status().isNoContent());

    }

    @SneakyThrows
    @Test
    void whenUpdateBoardById_thenReturn200() {

        final Long boardId = 1L;

        final Board boardReturned = Fixture.from(Board.class)
                .gimme(BoardTemplateLoader.BOARD_UPDATE_RETURN);

        final UpdateBoardRequest updateBoardRequest = Fixture.from(UpdateBoardRequest.class)
                .gimme(BoardTemplateLoader.UPDATE_BOARD_REQUEST);

        given(updateBoardService.updateBoard(any(Board.class)))
                .willReturn(boardReturned);

        mockMvc.perform(put(BoardController.ENDPOINT_BOARDS_BY_ID, boardId)
                .content(objectMapper.writeValueAsString(updateBoardRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(UPDATE_BOARD_RESPONSE_JSON));

        verify(updateBoardService).updateBoard(boardCaptor.capture());

        final Board boardCaptured = boardCaptor.getValue();

        assertThat(boardCaptured).isNotNull();
        assertThat(boardCaptured.getId()).isEqualTo(boardId);
        assertThat(boardCaptured.getName()).isEqualTo(updateBoardRequest.getName());

    }

}

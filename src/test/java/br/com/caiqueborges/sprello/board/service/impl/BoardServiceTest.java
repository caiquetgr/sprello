package br.com.caiqueborges.sprello.board.service.impl;

import br.com.caiqueborges.sprello.base.web.service.JwtRequestService;
import br.com.caiqueborges.sprello.board.exception.BoardNotFoundException;
import br.com.caiqueborges.sprello.board.fixture.BoardTemplateLoader;
import br.com.caiqueborges.sprello.board.repository.BoardRepository;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.mapper.BoardMapper;
import br.com.caiqueborges.sprello.board.service.mapper.BoardMapperImpl;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private JwtRequestService jwtRequestService;

    @Spy
    private BoardMapper boardMapper = new BoardMapperImpl();

    @InjectMocks
    private BoardService service;

    @BeforeAll
    public static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.board.fixture");
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @Test
    void whenRepositorySave_thenReturnCreatedBoard() {

        final Board boardPreInsert = loadFixture(BoardTemplateLoader.PRE_INSERT);
        final Board boardAfterInsert = loadFixture(BoardTemplateLoader.AFTER_INSERT);

        doReturn(boardAfterInsert)
                .when(boardRepository)
                .save(boardPreInsert);

        final Board returnedBoard = service.createBoard(boardPreInsert);

        assertThat(returnedBoard).isEqualToComparingFieldByField(boardAfterInsert);
        verify(boardRepository).save(boardPreInsert);

    }

    private Board loadFixture(String fixtureName) {
        return Fixture.from(Board.class).gimme(fixtureName);
    }

    @Test
    void whenGetBoardById_thenReturnBoard() {

        final Long boardId = 1L;
        final Long userId = 1L;

        final Board board = loadFixture(BoardTemplateLoader.AFTER_INSERT);

        doReturn(userId)
                .when(jwtRequestService).getRequestUserId();

        doReturn(Optional.of(board))
                .when(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);

        final Board boardFound = service.getBoardById(boardId);

        assertThat(boardFound).isEqualTo(board);

        verify(jwtRequestService).getRequestUserId();
        verify(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);

    }

    @Test
    void whenGetBoardById_AndReturnsEmptyOptional_thenShouldThrowBoardNotFoundException() {

        final Long boardId = 1L;
        final Long userId = 1L;

        doReturn(userId)
                .when(jwtRequestService).getRequestUserId();

        doReturn(Optional.empty())
                .when(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);

        assertThatThrownBy(() -> service.getBoardById(boardId))
                .isInstanceOf(BoardNotFoundException.class);

        verify(jwtRequestService).getRequestUserId();
        verify(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);

    }

    @Test
    void whenDeleteBoardById_thenDoNothing() {

        final Long boardId = 1L;
        final Long userId = 1L;
        final Board board = loadFixture(BoardTemplateLoader.AFTER_INSERT);

        willReturn(userId)
                .given(jwtRequestService).getRequestUserId();

        doReturn(Optional.of(board))
                .when(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);

        willDoNothing()
                .given(boardRepository)
                .deleteLogicallyById(board);

        assertThatCode(() -> service.deleteBoardById(boardId))
                .doesNotThrowAnyException();

        verify(jwtRequestService).getRequestUserId();
        verify(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);
        verify(boardRepository).deleteLogicallyById(board);

    }

    @Test
    void whenUpdateBoard_andBoardExists_thenReturnBoardUpdated() {

        final Long boardId = Long.valueOf(1L);
        final Long userId = 1L;
        final Board boardPreUpdate = loadFixture(BoardTemplateLoader.PRE_UPDATE);
        final Board boardPersisted = loadFixture(BoardTemplateLoader.AFTER_INSERT);

        given(jwtRequestService.getRequestUserId())
                .willReturn(userId);

        doReturn(Optional.of(boardPersisted))
                .when(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);

        given(boardRepository.save(boardPersisted))
                .willAnswer(answer -> answer.getArguments()[0]);

        final Board boardReturned = service.updateBoard(boardPreUpdate);

        verify(jwtRequestService).getRequestUserId();
        verify(boardRepository).findByIdAndCreatedByIdAndDeletedFalse(boardId, userId);
        verify(boardRepository).save(boardPersisted);

        assertThat(boardReturned).isNotNull();
        assertThat(boardReturned.getId()).isEqualTo(boardId);
        assertThat(boardReturned.getName()).isEqualTo(boardPreUpdate.getName());

        final Board boardAfterInsertFixture = loadFixture(BoardTemplateLoader.AFTER_INSERT);

        assertThat(boardReturned.getCreatedDate()).isEqualTo(boardAfterInsertFixture.getCreatedDate());
        assertThat(boardReturned.getLastModifiedDate()).isEqualTo(boardAfterInsertFixture.getLastModifiedDate());
        assertThat(boardReturned.getCreatedBy()).isEqualTo(boardAfterInsertFixture.getCreatedBy());
        assertThat(boardReturned.getCreatedById()).isEqualTo(boardAfterInsertFixture.getCreatedById());
        assertThat(boardReturned.getLastModifiedBy()).isEqualTo(boardAfterInsertFixture.getLastModifiedBy());
        assertThat(boardReturned.getLastModifiedById()).isEqualTo(boardAfterInsertFixture.getLastModifiedById());
        assertThat(boardReturned.getDeleted()).isEqualTo(boardAfterInsertFixture.getDeleted());

    }

}

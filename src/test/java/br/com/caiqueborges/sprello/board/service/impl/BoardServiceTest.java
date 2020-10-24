package br.com.caiqueborges.sprello.board.service.impl;

import br.com.caiqueborges.sprello.board.fixture.BoardTemplateLoader;
import br.com.caiqueborges.sprello.board.repository.BoardRepository;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService service;

    @BeforeAll
    public static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.board.fixture");
    }

    @Test
    void whenRepositorySave_thenReturnCreatedBoard() {

        Board boardPreInsert = loadFixture(BoardTemplateLoader.PRE_INSERT);
        Board boardAfterInsert = loadFixture(BoardTemplateLoader.AFTER_INSERT);

        doReturn(boardAfterInsert)
                .when(boardRepository)
                .save(boardPreInsert);

        Board returnedBoard = service.createBoard(boardPreInsert);

        assertThat(returnedBoard).isEqualToComparingFieldByField(boardAfterInsert);
        verify(boardRepository).save(boardPreInsert);

    }

    private Board loadFixture(String fixtureName) {
        return Fixture.from(Board.class).gimme(fixtureName);
    }

}

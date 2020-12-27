package br.com.caiqueborges.sprello.board.repository;

import br.com.caiqueborges.sprello.board.fixture.BoardTemplateLoader;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.sql.BoardSql;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.caiqueborges.sprello.user.sql.UserSql;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository repository;

    @BeforeAll
    public static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.board.fixture");
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @Sql(UserSql.CREATE_USER_1_SQL)
    @Test
    void whenSave_thenReturnCreatedBoard() {

        Board board = Fixture.from(Board.class).gimme(BoardTemplateLoader.PRE_INSERT);
        Board boardCreated = repository.save(board);

        assertThat(boardCreated).isNotNull();
        assertThat(boardCreated.getId()).isNotNull();
        assertThat(boardCreated.getName()).isEqualTo(board.getName());
        validateUserAudit(boardCreated.getCreatedBy());
        validateUserAudit(boardCreated.getLastModifiedBy());
        assertThat(boardCreated.getDeleted()).isFalse();
        assertThat(boardCreated.getCreatedDate()).isNotNull();
        assertThat(boardCreated.getLastModifiedDate()).isNotNull();

    }

    private void validateUserAudit(User user) {
        assertThat(user)
                .isNotNull()
                .extracting(User::getId)
                .isEqualTo(Long.valueOf(1L));
    }

    @Sql(scripts = {UserSql.CREATE_USER_1_SQL, BoardSql.INSERT_BOARD})
    @Test
    void whenFindById_thenReturnBoard() {

        final Long boardId = 1L;
        final String boardName = "Test board";
        final ZonedDateTime createdAndModifiedDate = ZonedDateTime.of(
                LocalDate.of(2020, Month.MAY, 12),
                LocalTime.of(0, 46, 50, 347659000),
                ZoneId.of("UTC")
        );

        final Board board = repository.findById(boardId).orElse(null);

        assertThat(board).isNotNull();
        assertThat(board.getId()).isEqualTo(boardId);
        assertThat(board.getName()).isEqualTo(boardName);
        validateUserAudit(board.getCreatedBy());
        validateUserAudit(board.getLastModifiedBy());
        assertThat(board.getDeleted()).isFalse();
        assertThat(board.getCreatedDate()).isEqualTo(createdAndModifiedDate);
        assertThat(board.getLastModifiedDate()).isEqualTo(createdAndModifiedDate);

    }

}

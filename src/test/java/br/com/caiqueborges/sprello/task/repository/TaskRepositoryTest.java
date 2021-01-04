package br.com.caiqueborges.sprello.task.repository;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.sql.BoardSql;
import br.com.caiqueborges.sprello.task.fixture.TaskTemplateLoader;
import br.com.caiqueborges.sprello.task.repository.entity.Task;
import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import br.com.caiqueborges.sprello.user.sql.UserSql;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.caiqueborges.sprello.util.TestUtils.loadFixture;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.board.fixture");
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.task.fixture");
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @Sql(scripts = {UserSql.CREATE_USER_1_SQL, BoardSql.INSERT_BOARD})
    @Test
    void whenSave_thenReturnCreatedTask() {

        final Task task = loadFixture(TaskTemplateLoader.PRE_INSERT, Task.class);
        final Task taskCreated = taskRepository.save(task);

        entityManager.flush();

        assertThat(taskCreated).isNotNull();
        assertThat(taskCreated.getId()).isEqualTo(1L);
        assertThat(taskCreated.getBoardId()).isEqualTo(1L);
        assertThat(taskCreated.getName()).isEqualTo("New task");
        assertThat(taskCreated.getDescription()).isEqualTo("The new task description");

        final Board board = taskCreated.getBoard();

        assertThat(board).isNotNull();
        assertThat(board).extracting(Board::getId).isEqualTo(1L);

        final TaskStatus taskStatus = taskCreated.getTaskStatus();

        assertThat(taskStatus).isNotNull();
        assertThat(taskStatus.getId()).isEqualTo(1L);

    }

}

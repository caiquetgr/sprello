package br.com.caiqueborges.sprello.task.fixture;

import br.com.caiqueborges.sprello.board.fixture.BoardTemplateLoader;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.task.controller.model.CreateTaskRequest;
import br.com.caiqueborges.sprello.task.repository.entity.Task;
import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import br.com.caiqueborges.sprello.user.fixture.UserTemplateLoader;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TaskTemplateLoader implements TemplateLoader {

    public static final String CREATE_TASK_REQUEST_VALID = "CREATE_TASK_REQUEST_VALID";
    public static final String CREATE_TASK_REQUEST_VALID_ENTITY_RETURN = "CREATE_TASK_REQUEST_VALID_ENTITY_RETURN";

    public static final String PRE_INSERT = "TASK_PRE_INSERT";
    public static final String AFTER_INSERT = "TASK_AFTER_INSERT";

    @Override
    public void load() {
        addTemplatesCreateBoardRequest();
        addTemplatesPreAndAfterInsert();
    }

    private void addTemplatesCreateBoardRequest() {

        final String taskName = "New task";
        final String taskDescription = "The new task description";

        Fixture.of(CreateTaskRequest.class).addTemplate(CREATE_TASK_REQUEST_VALID, new Rule() {{
            add("name", taskName);
            add("description", taskDescription);
        }});

        Fixture.of(Task.class).addTemplate(CREATE_TASK_REQUEST_VALID_ENTITY_RETURN, new Rule() {{
            add("id", 1L);
            add("boardId", 1L);
            add("name", taskName);
            add("description", taskDescription);
            add("taskStatus", one(TaskStatus.class, TaskStatusTemplateLoader.TASK_STATUS_TO_DO));
        }});

    }

    private void addTemplatesPreAndAfterInsert() {

        final String taskName = "New task";
        final String taskDescription = "The new task description";

        final ZonedDateTime insertionDate = ZonedDateTime.of(
                LocalDate.of(2020, Month.MAY, 12),
                LocalTime.of(21, 34, 50, 347659000),
                ZoneId.of("UTC")
        );

        Fixture.of(Task.class)
                .addTemplate(PRE_INSERT, new Rule() {{
                    add("boardId", 1L);
                    add("board", one(Board.class, BoardTemplateLoader.AFTER_INSERT));
                    add("name", taskName);
                    add("description", taskDescription);
                    add("taskStatus", one(TaskStatus.class, TaskStatusTemplateLoader.TASK_STATUS_TO_DO));
                    add("createdBy", one(User.class, UserTemplateLoader.AFTER_INSERT));
                    add("createdById", 1L);
                    add("lastModifiedBy", one(User.class, UserTemplateLoader.AFTER_INSERT));
                    add("lastModifiedById", 1L);
                    add("createdDate", insertionDate);
                    add("lastModifiedDate", insertionDate);
                    add("deleted", Boolean.FALSE);
                }});

        Fixture.of(Task.class)
                .addTemplate(AFTER_INSERT)
                .inherits(PRE_INSERT, new Rule() {{
                    add("id", 1L);
                }});

    }

}

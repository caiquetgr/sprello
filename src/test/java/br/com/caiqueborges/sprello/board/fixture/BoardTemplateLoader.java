package br.com.caiqueborges.sprello.board.fixture;

import br.com.caiqueborges.sprello.board.controller.model.UpdateBoardRequest;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.user.fixture.UserTemplateLoader;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BoardTemplateLoader implements TemplateLoader {

    public static final String PRE_INSERT = "preInsert";
    public static final String AFTER_INSERT = "afterInsert";

    public static final String UPDATE_BOARD_REQUEST = "updateBoardRequest";
    public static final String BOARD_UPDATE_RETURN = "boardUpdateReturn";

    @Override
    public void load() {

        final ZonedDateTime insertionDate = ZonedDateTime.of(
                LocalDate.of(2020, Month.MAY, 12),
                LocalTime.of(21, 34, 50, 347659000),
                ZoneId.of("UTC")
        );

        addPreInsert(insertionDate);
        addAfterInsert();
        addUpdateBoardRequest();
    }

    private void addAfterInsert() {

        Fixture.of(Board.class)
                .addTemplate(AFTER_INSERT)
                .inherits(PRE_INSERT, new Rule() {{
                    add("id", 1L);
                }});

    }

    private void addPreInsert(ZonedDateTime insertionDate) {

        Fixture.of(Board.class)
                .addTemplate(PRE_INSERT, new Rule() {{
                    add("name", "Test");
                    add("createdBy", one(User.class, UserTemplateLoader.AFTER_INSERT));
                    add("createdById", 1L);
                    add("lastModifiedBy", one(User.class, UserTemplateLoader.AFTER_INSERT));
                    add("lastModifiedById", 1L);
                    add("createdDate", insertionDate);
                    add("lastModifiedDate", insertionDate);
                    add("deleted", Boolean.FALSE);
                }});

    }

    private void addUpdateBoardRequest() {

        Fixture.of(UpdateBoardRequest.class)
                .addTemplate(UPDATE_BOARD_REQUEST, new Rule() {{
                    add("name", "Test update");
                }});

        Fixture.of(Board.class)
                .addTemplate(BOARD_UPDATE_RETURN)
                .inherits(AFTER_INSERT, new Rule() {{
                    add("name", "Test update");
                }});

    }

}

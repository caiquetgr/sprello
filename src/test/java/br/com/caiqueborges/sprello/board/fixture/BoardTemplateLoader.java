package br.com.caiqueborges.sprello.board.fixture;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.user.fixture.UserTemplateLoader;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.ZonedDateTime;

public class BoardTemplateLoader implements TemplateLoader {

    public static final String PRE_INSERT = "preInsert";
    public static final String AFTER_INSERT = "afterInsert";

    @Override
    public void load() {
        Fixture.of(Board.class)
                .addTemplate(PRE_INSERT, new Rule() {{
                    add("name", "Test");
                    add("createdBy", one(User.class, UserTemplateLoader.AFTER_INSERT));
                    add("lastModifiedBy", one(User.class, UserTemplateLoader.AFTER_INSERT));
                    add("createdDate", ZonedDateTime.now());
                    add("lastModifiedDate", ZonedDateTime.now());
                    add("deleted", Boolean.FALSE);
                }});

        Fixture.of(Board.class).addTemplate(AFTER_INSERT)
                .inherits(PRE_INSERT, new Rule() {{
                    add("id", 1L);
                }});
    }
}

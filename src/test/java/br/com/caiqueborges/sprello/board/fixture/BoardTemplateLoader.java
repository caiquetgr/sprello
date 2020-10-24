package br.com.caiqueborges.sprello.board.fixture;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class BoardTemplateLoader implements TemplateLoader {

    public static final String PRE_INSERT = "preInsert";
    public static final String AFTER_INSERT = "afterInsert";

    @Override
    public void load() {
        Fixture.of(Board.class)
                .addTemplate(PRE_INSERT, new Rule() {{
                    add("name", "Test");
                }});

        Fixture.of(Board.class).addTemplate(AFTER_INSERT)
                .inherits(PRE_INSERT, new Rule() {{
                    add("id", 1L);
                }});
    }
}

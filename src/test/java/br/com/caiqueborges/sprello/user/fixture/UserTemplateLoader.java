package br.com.caiqueborges.sprello.user.fixture;

import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UserTemplateLoader implements TemplateLoader {

    public static final String PRE_INSERT = "preInsert";
    public static final String AFTER_INSERT = "afterInsert";

    @Override
    public void load() {

        Fixture.of(User.class)
                .addTemplate(PRE_INSERT, new Rule() {{
                    add("firstName", "Caique");
                    add("lastName", "Aquino Borges");
                    add("email", "caiquetgr@gmail.com");
                }});

        Fixture.of(User.class)
                .addTemplate(AFTER_INSERT)
                .inherits(PRE_INSERT, new Rule() {{
                    add("id", Long.valueOf(1L));
                    add("password", "$!@#dasdcasbv");
                    add("creationDate", ZonedDateTime.of(2020, Month.MAY.getValue(),
                            12, 12, 00, 00, 000000, ZoneId.of("UTC")));
                    add("active", Boolean.TRUE);
                }});

    }
}

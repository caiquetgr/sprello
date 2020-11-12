package br.com.caiqueborges.sprello.user.fixture;

import br.com.caiqueborges.sprello.user.controller.model.CreateUserRequest;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class CreateUserRequestTemplateLoader implements TemplateLoader {

    public static final String VALID_CREATE_USER_REQUEST = "validCreateUserRequest";

    @Override
    public void load() {

        Fixture.of(CreateUserRequest.class)
                .addTemplate(VALID_CREATE_USER_REQUEST, new Rule() {{
                    add("firstName", "Caique");
                    add("lastName", "Aquino Borges");
                    add("email", "caiquetgr@gmail.com");
                    add("password", "orewa12#$!@");
                }});

    }

}

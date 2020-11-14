package br.com.caiqueborges.sprello.security.fixture;

import br.com.caiqueborges.sprello.security.controller.model.LoginRequest;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class LoginRequestTemplateLoader implements TemplateLoader {

    public static final String VALID_LOGIN_REQUEST = "validLoginRequest";

    @Override
    public void load() {

        Fixture.of(LoginRequest.class).addTemplate(VALID_LOGIN_REQUEST, new Rule() {{
            add("email", "test@test.com");
            add("password", "pass123098");
        }});

    }

}

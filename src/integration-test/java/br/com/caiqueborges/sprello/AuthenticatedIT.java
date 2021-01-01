package br.com.caiqueborges.sprello;

import br.com.caiqueborges.sprello.base.service.JwtService;
import br.com.caiqueborges.sprello.login.service.model.JwtInfo;
import br.com.caiqueborges.sprello.user.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static br.com.caiqueborges.sprello.login.LoginIT.CREATE_VALID_USER_SQL;

@Sql(CREATE_VALID_USER_SQL)
public abstract class AuthenticatedIT extends AbstractIT {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    protected String getJwtToken() {

        return userRepository.findByEmail("caiquetgr@gmail.com")
                .map(jwtService::createJwt)
                .map(JwtInfo::getAuthorizationToken)
                .orElse(null);

    }

    @SneakyThrows
    protected ResultActions performAuthenticated(MockHttpServletRequestBuilder requestBuilder) {
        return this.mockMvc.perform(requestBuilder.header(HttpHeaders.AUTHORIZATION, getJwtToken()));
    }

}

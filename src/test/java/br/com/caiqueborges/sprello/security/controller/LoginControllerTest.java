package br.com.caiqueborges.sprello.security.controller;

import br.com.caiqueborges.sprello.security.controller.mapper.LoginControllerMapperImpl;
import br.com.caiqueborges.sprello.security.controller.model.LoginRequest;
import br.com.caiqueborges.sprello.security.fixture.LoginRequestTemplateLoader;
import br.com.caiqueborges.sprello.security.service.LoginService;
import br.com.caiqueborges.sprello.security.service.model.AuthenticationInfo;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static br.com.caiqueborges.sprello.util.JsonUnitUtils.JSON_FOLDER;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    private static final String LOGIN_JSON_FOLDER = JSON_FOLDER + "login/controller";

    @MockBean
    private LoginService loginService;

    @SpyBean
    private LoginControllerMapperImpl loginControllerMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.security.fixture");
    }

    @SneakyThrows
    @Test
    void whenLogin_thenReturnAuthenticationToken() {

        final AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .authenticationToken("token")
                .userId(1L)
                .validUntil(ZonedDateTime.parse("2020-11-14T20:33:09.303243Z"))
                .build();

        LoginRequest loginRequest = Fixture.from(LoginRequest.class)
                .gimme(LoginRequestTemplateLoader.VALID_LOGIN_REQUEST);

        given(loginService.login(loginRequest.getEmail(), loginRequest.getPassword()))
                .willReturn(authenticationInfo);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonIsEqualToFile(LOGIN_JSON_FOLDER + "/login-response.json"));

    }

}

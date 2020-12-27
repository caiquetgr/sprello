package br.com.caiqueborges.sprello.security;

import br.com.caiqueborges.sprello.AbstractIT;
import br.com.caiqueborges.sprello.security.controller.model.LoginRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginIT extends AbstractIT {

    private static final String SECURITY_FOLDER = "security/";
    private static final String JSON_SECURITY_FOLDER = JSON_FOLDER + SECURITY_FOLDER;
    private static final String SQL_SECURITY_FOLDER = SQL_FOLDER + SECURITY_FOLDER;

    public static final String CREATE_VALID_USER_SQL = SQL_SECURITY_FOLDER + "create-valid-user.sql";
    public static final String CREATE_INACTIVE_USER_SQL = SQL_SECURITY_FOLDER + "create-inactive-user.sql";

    @SneakyThrows
    @Sql(CREATE_VALID_USER_SQL)
    @Test
    void whenLogin_withValidUser_thenReturn200AndLoginResponse() {

        final LoginRequest validLoginRequest = getValidLoginRequest();

        final MockHttpServletResponse response = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

    }

    private LoginRequest getValidLoginRequest() {
        return readJsonToObject(JSON_SECURITY_FOLDER.concat("login-valid-request.json"), LoginRequest.class);
    }

    @SneakyThrows
    @Sql(CREATE_VALID_USER_SQL)
    @Test
    void whenLogin_withInvalidPassword_thenReturn401() {

        final LoginRequest loginRequest = getValidLoginRequest();

        loginRequest.setPassword("ascas1bv568a97c");

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

    }

    @SneakyThrows
    @Sql(CREATE_INACTIVE_USER_SQL)
    @Test
    void whenLogin_withUserInactive_thenReturn401() {

        final LoginRequest loginRequest = getValidLoginRequest();

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

    }

    @SneakyThrows
    @Test
    void whenLogin_withNonExistantUser_thenReturn401() {

        final LoginRequest nonExistantUser = new LoginRequest("caique123@gmail.com", "cjajpawi2A!@#");

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nonExistantUser)))
                .andExpect(status().isUnauthorized());

    }

}

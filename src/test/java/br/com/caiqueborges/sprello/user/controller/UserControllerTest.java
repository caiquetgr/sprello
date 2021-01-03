package br.com.caiqueborges.sprello.user.controller;

import br.com.caiqueborges.sprello.base.BaseTestController;
import br.com.caiqueborges.sprello.user.controller.mapper.UserControllerMapperImpl;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserRequest;
import br.com.caiqueborges.sprello.user.fixture.CreateUserRequestTemplateLoader;
import br.com.caiqueborges.sprello.user.fixture.UserTemplateLoader;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.caiqueborges.sprello.user.service.CreateUserService;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.caiqueborges.sprello.util.JsonUnitUtils.JSON_FOLDER;
import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static br.com.caiqueborges.sprello.util.TestUtils.loadFixture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
class UserControllerTest extends BaseTestController {

    private static final String USER_JSON_FOLDER = JSON_FOLDER + "user/controller";

    @MockBean
    private CreateUserService createUserService;

    @SpyBean
    private UserControllerMapperImpl userControllerMapper;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @SneakyThrows
    @Test
    void whenValidInput_thenReturnStatus201AndBodyEntity() {

        final CreateUserRequest createUserRequest = loadFixture(CreateUserRequestTemplateLoader.VALID_CREATE_USER_REQUEST,
                CreateUserRequest.class);

        final User createdUser = loadFixture(UserTemplateLoader.AFTER_INSERT, User.class);

        given(createUserService.createUser(any(User.class)))
                .willReturn(createdUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonIsEqualToFile(USER_JSON_FOLDER + "/create-user-response.json"));

    }

}

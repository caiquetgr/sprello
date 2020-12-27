package br.com.caiqueborges.sprello.user;

import br.com.caiqueborges.sprello.AbstractIT;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserRequest;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static br.com.caiqueborges.sprello.util.JsonUnitUtils.jsonIsEqualToFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIT extends AbstractIT {

    private static final String USERS_ENDPOINT = "/users";

    private static final String JSON_USER_FOLDER = JSON_FOLDER + "user/";
    private static final String CREATE_USER_VALID_REQUEST_JSON = JSON_USER_FOLDER + "create-user-valid-request.json";
    private static final String CREATE_USER_VALID_RESPONSE_JSON = JSON_USER_FOLDER + "create-user-valid-response.json";

    @SneakyThrows
    @Test
    void whenCreateUser_withValidCreateUserRequest_thenReturn201AndCreatedEntity() {

        this.mockMvc.perform(post(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(CREATE_USER_VALID_REQUEST_JSON)))
                .andExpect(status().isCreated())
                .andExpect(jsonIsEqualToFile(CREATE_USER_VALID_RESPONSE_JSON));

    }

    @SneakyThrows
    @Test
    void whenCreateUser_withInvalidEmail_thenReturn400() {

        final CreateUserRequest createUserRequest = getValidCreateUserRequest();

        createUserRequest.setEmail("c");
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setEmail(StringUtils.EMPTY);
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setEmail(null);
        callCreateUserAndExpectBadRequest(createUserRequest);

    }

    private CreateUserRequest getValidCreateUserRequest() {
        return readJsonToObject(CREATE_USER_VALID_REQUEST_JSON, CreateUserRequest.class);
    }

    private void callCreateUserAndExpectBadRequest(CreateUserRequest createUserRequest) throws Exception {

        this.mockMvc.perform(post(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void whenCreateUser_withInvalidPassword_thenReturn400() {

        final CreateUserRequest createUserRequest = getValidCreateUserRequest();

        createUserRequest.setPassword("c");
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setPassword(StringUtils.EMPTY);
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setPassword(null);
        callCreateUserAndExpectBadRequest(createUserRequest);

    }

    @SneakyThrows
    @Test
    void whenCreateUser_withInvalidFirstName_thenReturn400() {

        final CreateUserRequest createUserRequest = getValidCreateUserRequest();

        createUserRequest.setFirstName(RandomStringUtils.random(81, true, false));
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setFirstName(StringUtils.EMPTY);
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setFirstName(null);
        callCreateUserAndExpectBadRequest(createUserRequest);

    }

    @SneakyThrows
    @Test
    void whenCreateUser_withInvalidLastName_thenReturn400() {

        final CreateUserRequest createUserRequest = getValidCreateUserRequest();

        createUserRequest.setLastName(RandomStringUtils.random(301, true, false));
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setLastName(StringUtils.EMPTY);
        callCreateUserAndExpectBadRequest(createUserRequest);

        createUserRequest.setLastName(null);
        callCreateUserAndExpectBadRequest(createUserRequest);

    }

}
